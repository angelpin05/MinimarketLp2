package pe.edu.cibertec.minimarket.service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.TreeMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.minimarket.model.*;
import pe.edu.cibertec.minimarket.repository.*;
import pe.edu.cibertec.minimarket.web.form.VentaForm;

@Service
public class VentaService {
    private final VentaRepository ventas;
    private final ProductoRepository productos;
    private final ClienteRepository clientes;
    private final UsuarioRepository usuarios;
    public VentaService(VentaRepository v,ProductoRepository p,ClienteRepository c,UsuarioRepository u) {ventas=v;productos=p;clientes=c;usuarios=u;}
    @Transactional public Long registrar(VentaForm form,String username) {
        if(form.getToken()==null || form.getToken().length()!=36 || ventas.existsByToken(form.getToken())) throw new ReglaNegocioException("Formulario ya utilizado o inválido. Abre una venta nueva.");
        var cliente=clientes.findById(form.getClienteId()).orElseThrow(()->new ReglaNegocioException("Cliente no encontrado"));
        if(!cliente.isActivo()) throw new ReglaNegocioException("El cliente está inactivo");
        var usuario=usuarios.findByUsername(username).orElseThrow(()->new ReglaNegocioException("Usuario no encontrado"));
        if(!usuario.isActivo()) throw new ReglaNegocioException("El usuario está inactivo");
        if(form.getLineas()==null || form.getLineas().isEmpty() || form.getLineas().size()>30) throw new ReglaNegocioException("Agrega entre 1 y 30 líneas de venta");
        var cantidades=new TreeMap<Long,Integer>();
        for(var linea:form.getLineas()) {
            if(linea.getProductoId()==null || linea.getCantidad()==null || linea.getCantidad()<1 || linea.getCantidad()>100000) throw new ReglaNegocioException("Selecciona un producto y una cantidad válida");
            int acumulada=cantidades.getOrDefault(linea.getProductoId(),0)+linea.getCantidad();
            if(acumulada>100000) throw new ReglaNegocioException("Cantidad acumulada demasiado grande");
            cantidades.put(linea.getProductoId(),acumulada);
        }
        var venta=new Venta();venta.setCliente(cliente);venta.setUsuario(usuario);venta.setFecha(LocalDateTime.now(java.time.ZoneId.of("America/Lima")));venta.setToken(form.getToken());
        BigDecimal total=BigDecimal.ZERO;
        // Se bloquea por ID ascendente para reducir el riesgo de interbloqueos.
        for(var e:cantidades.entrySet()) {
            var producto=productos.bloquearPorId(e.getKey()).orElseThrow(()->new ReglaNegocioException("Producto no encontrado"));
            if(!producto.isActivo() || !producto.getCategoria().isActivo()) throw new ReglaNegocioException("Producto o categoría inactiva: "+producto.getNombre());
            if(producto.getStock()<e.getValue()) throw new ReglaNegocioException("Stock insuficiente para "+producto.getNombre()+". Disponible: "+producto.getStock());
            var detalle=new DetalleVenta();detalle.setVenta(venta);detalle.setProducto(producto);detalle.setNombreProducto(producto.getNombre());
            detalle.setCantidad(e.getValue());detalle.setPrecioUnitario(producto.getPrecio());detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(e.getValue())));
            venta.getDetalles().add(detalle);total=total.add(detalle.getSubtotal());producto.setStock(producto.getStock()-e.getValue());
        }
        if(total.compareTo(new BigDecimal("9999999999.99"))>0) throw new ReglaNegocioException("Total de venta fuera de rango");
        venta.setTotal(total);
        // Precio y total se calculan en el servidor. El navegador solo envía IDs y cantidades.
        return ventas.saveAndFlush(venta).getId();
    }
}
