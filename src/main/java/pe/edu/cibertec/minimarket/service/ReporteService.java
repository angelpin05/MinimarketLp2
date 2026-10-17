package pe.edu.cibertec.minimarket.service;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.*;
import pe.edu.cibertec.minimarket.repository.ProductoRepository;

@Service
public class ReporteService {
    private final ProductoRepository productos;
    private final ConsultaService consultas;
    public ReporteService(ProductoRepository p,ConsultaService c){productos=p;consultas=c;}
    @Transactional(readOnly=true)
    public byte[] inventario(Long categoria,String texto,boolean bajo) throws JRException {
        var filas=new ArrayList<Map<String,?>>();
        for(var p:productos.consultar(categoria,texto,bajo)) {
            filas.add(Map.of("codigo",p.getCodigo(),"nombre",p.getNombre(),"categoria",p.getCategoria().getNombre(),
                "stock",String.valueOf(p.getStock()),"minimo",String.valueOf(p.getStockMinimo()),"precio",p.getPrecio().toPlainString()));
        }
        String filtro="Categoría: "+(categoria==null?"Todas":categoria)+"    Búsqueda: "+texto+"    Stock bajo: "+(bajo?"Sí":"No");
        return pdf(disenoInventario(),filas,filtro,"Productos: "+filas.size());
    }
    @Transactional(readOnly=true)
    public byte[] ventas(LocalDate desde,LocalDate hasta,Long cliente) throws JRException {
        var filas=new ArrayList<Map<String,?>>();BigDecimal total=BigDecimal.ZERO;
        for(var v:consultas.ventas(desde,hasta,cliente)) {
            filas.add(Map.of("id",String.valueOf(v.getId()),"fecha",v.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                "cliente",v.getCliente().getNombre(),"vendedor",v.getUsuario().getUsername(),"total",v.getTotal().toPlainString()));total=total.add(v.getTotal());
        }
        return pdf(disenoVentas(),filas,"Desde: "+desde+"    Hasta: "+hasta+"    Cliente: "+(cliente==null?"Todos":cliente),"Ventas: "+filas.size()+"    Total S/: "+total.toPlainString());
    }
    private byte[] pdf(JasperDesign diseno,ArrayList<Map<String,?>> filas,String filtro,String resumen) throws JRException {
        var parametros=new HashMap<String,Object>();parametros.put("FILTRO",filtro);parametros.put("RESUMEN",resumen);
        parametros.put("EMISION",java.time.ZonedDateTime.now(java.time.ZoneId.of("America/Lima")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        var informe=JasperCompileManager.compileReport(diseno);
        var impreso=JasperFillManager.fillReport(informe,parametros,new JRMapCollectionDataSource(filas));
        return JasperExportManager.exportReportToPdf(impreso);
    }
    // Ambos diseños son reportes JasperReports reales. Se pueden exportar a JRXML para Jaspersoft Studio.
    public static JasperDesign disenoInventario() throws JRException {
        return diseno("inventario","Reporte de inventario",new String[]{"codigo","nombre","categoria","stock","minimo","precio"},
            new String[]{"Código","Producto","Categoría","Stock","Mínimo","S/"},new int[]{65,175,120,45,60,70});
    }
    public static JasperDesign disenoVentas() throws JRException {
        return diseno("ventas","Reporte de ventas",new String[]{"id","fecha","cliente","vendedor","total"},
            new String[]{"Venta","Fecha","Cliente","Vendedor","S/"},new int[]{45,120,190,100,80});
    }
    private static JasperDesign diseno(String nombre,String titulo,String[] campos,String[] etiquetas,int[] anchos) throws JRException {
        var d=new JasperDesign();d.setName(nombre);d.setPageWidth(595);d.setPageHeight(842);d.setColumnWidth(535);
        d.setLeftMargin(30);d.setRightMargin(30);d.setTopMargin(30);d.setBottomMargin(30);d.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
        var estilo=new JRDesignStyle();estilo.setName("Base");estilo.setDefault(true);estilo.setFontName("DejaVu Sans");estilo.setFontSize(10f);d.addStyle(estilo);
        for(String p:List.of("FILTRO","RESUMEN","EMISION")){var param=new JRDesignParameter();param.setName(p);param.setValueClass(String.class);d.addParameter(param);}
        var cabecera=new JRDesignBand();cabecera.setHeight(85);cabecera.addElement(texto(titulo,0,0,535,30,18,true));
        cabecera.addElement(campo("$P{FILTRO}",0,33,535,25));cabecera.addElement(campo("\"Emitido en Lima: \"+$P{EMISION}",0,60,535,20));d.setTitle(cabecera);
        var columnas=new JRDesignBand();columnas.setHeight(28);var detalle=new JRDesignBand();detalle.setHeight(38);int x=0;
        for(int i=0;i<campos.length;i++){
            var f=new JRDesignField();f.setName(campos[i]);f.setValueClass(String.class);d.addField(f);
            columnas.addElement(texto(etiquetas[i],x,0,anchos[i]-5,26,10,true));detalle.addElement(campo("$F{"+campos[i]+"}",x,0,anchos[i]-5,36));x+=anchos[i];
        }
        d.setColumnHeader(columnas);((JRDesignSection)d.getDetailSection()).addBand(detalle);
        var resumen=new JRDesignBand();resumen.setHeight(40);resumen.addElement(campo("$P{RESUMEN}",0,8,535,30));d.setSummary(resumen);
        var pie=new JRDesignBand();pie.setHeight(24);pie.addElement(campo("\"Página \"+$V{PAGE_NUMBER}",440,0,95,24));d.setPageFooter(pie);
        return d;
    }
    private static JRDesignStaticText texto(String t,int x,int y,int w,int h,int size,boolean bold){
        var e=new JRDesignStaticText();e.setX(x);e.setY(y);e.setWidth(w);e.setHeight(h);e.setText(t);e.setFontSize((float)size);e.setBold(bold);return e;
    }
    private static JRDesignTextField campo(String expr,int x,int y,int w,int h){
        var e=new JRDesignTextField();e.setX(x);e.setY(y);e.setWidth(w);e.setHeight(h);e.setTextAdjust(TextAdjustEnum.STRETCH_HEIGHT);
        e.setExpression(new JRDesignExpression(expr));e.setBlankWhenNull(true);return e;
    }
}
