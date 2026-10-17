package pe.edu.cibertec.minimarket;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;
import pe.edu.cibertec.minimarket.model.*;
import pe.edu.cibertec.minimarket.repository.*;
import pe.edu.cibertec.minimarket.service.*;
import pe.edu.cibertec.minimarket.web.form.*;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProyectoIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired VentaService servicio;
    @Autowired ReporteService reportes;
    @Autowired CatalogoService catalogo;
    @Autowired ConsultaService consultas;
    @Autowired ProductoRepository productos;
    @Autowired CategoriaRepository categorias;
    @Autowired ClienteRepository clientes;
    @Autowired VentaRepository ventas;

    private Producto producto(int stock) {
        var p=new Producto();p.setCodigo("T"+UUID.randomUUID().toString().substring(0,25));p.setNombre("Producto de prueba");
        p.setCategoria(categorias.findById(1L).orElseThrow());p.setPrecio(new BigDecimal("4.50"));p.setStock(stock);p.setStockMinimo(2);
        return productos.saveAndFlush(p);
    }
    private VentaForm form(Long id,int cantidad) {
        var f=new VentaForm();f.setClienteId(1L);f.setToken(UUID.randomUUID().toString());agregar(f,id,cantidad);return f;
    }
    private void agregar(VentaForm f,Long id,int cantidad) {
        var l=new VentaForm.Linea();l.setProductoId(id);l.setCantidad(cantidad);f.getLineas().add(l);
    }

    @Test void ventaGuardaDetalleTotalYDescuentaStock() {
        var p=producto(10);Long id=servicio.registrar(form(p.getId(),3),"vendedor");
        var venta=ventas.buscarDetalle(id).orElseThrow();
        assertThat(venta.getTotal()).isEqualByComparingTo("13.50");assertThat(venta.getDetalles()).hasSize(1);
        assertThat(venta.getDetalles().get(0).getSubtotal()).isEqualByComparingTo("13.50");
        assertThat(productos.findById(p.getId()).orElseThrow().getStock()).isEqualTo(7);
    }
    @Test void errorEnSegundaLineaRevierteTodaLaOperacion() {
        var primero=producto(10);var segundo=producto(1);long antes=ventas.count();
        var f=form(primero.getId(),3);agregar(f,segundo.getId(),2);
        assertThatThrownBy(()->servicio.registrar(f,"admin")).isInstanceOf(ReglaNegocioException.class).hasMessageContaining("Stock insuficiente");
        assertThat(ventas.count()).isEqualTo(antes);
        assertThat(productos.findById(primero.getId()).orElseThrow().getStock()).isEqualTo(10);
        assertThat(productos.findById(segundo.getId()).orElseThrow().getStock()).isEqualTo(1);
    }
    @Test void productosRepetidosSumanCantidadesAntesDeValidarStock() {
        var p=producto(5);var f=form(p.getId(),3);agregar(f,p.getId(),3);
        assertThatThrownBy(()->servicio.registrar(f,"admin")).isInstanceOf(ReglaNegocioException.class);
        assertThat(productos.findById(p.getId()).orElseThrow().getStock()).isEqualTo(5);
        f.getLineas().get(1).setCantidad(2);Long id=servicio.registrar(f,"admin");
        assertThat(ventas.buscarDetalle(id).orElseThrow().getDetalles()).hasSize(1);
        assertThat(productos.findById(p.getId()).orElseThrow().getStock()).isZero();
    }
    @Test void tokenRepetidoNoDuplicaVenta() {
        var p=producto(5);var f=form(p.getId(),2);servicio.registrar(f,"admin");long antes=ventas.count();
        assertThatThrownBy(()->servicio.registrar(f,"admin")).isInstanceOf(ReglaNegocioException.class);
        assertThat(ventas.count()).isEqualTo(antes);assertThat(productos.findById(p.getId()).orElseThrow().getStock()).isEqualTo(3);
    }
    @Test void dosVentasConcurrentesNoVendenMasQueElStock() throws Exception {
        var p=producto(5);var inicio=new CountDownLatch(1);var pool=Executors.newFixedThreadPool(2);
        Callable<Boolean> vender=()->{inicio.await();try{servicio.registrar(form(p.getId(),4),"admin");return true;}catch(ReglaNegocioException e){return false;}};
        try {
            var primera=pool.submit(vender);var segunda=pool.submit(vender);inicio.countDown();
            int exitos=(primera.get(20,TimeUnit.SECONDS)?1:0)+(segunda.get(20,TimeUnit.SECONDS)?1:0);
            assertThat(exitos).isEqualTo(1);assertThat(productos.findById(p.getId()).orElseThrow().getStock()).isEqualTo(1);
        }finally{pool.shutdownNow();}
    }
    @Test void formularioAntiguoNoSobrescribeStockDeUnaVenta() {
        var p=producto(5);var f=new ProductoForm();f.setCodigo(p.getCodigo());f.setNombre(p.getNombre());f.setCategoriaId(1L);
        f.setPrecio(p.getPrecio());f.setStock(5);f.setStockMinimo(2);f.setVersion(p.getVersion());
        servicio.registrar(form(p.getId(),2),"admin");
        assertThatThrownBy(()->catalogo.guardarProducto(p.getId(),f)).isInstanceOf(ReglaNegocioException.class).hasMessageContaining("cambió");
        assertThat(productos.findById(p.getId()).orElseThrow().getStock()).isEqualTo(3);
    }
    @Test void productoInactivoNoSePuedeVender() {
        var p=producto(5);catalogo.desactivarProducto(p.getId());
        assertThatThrownBy(()->servicio.registrar(form(p.getId(),1),"admin")).isInstanceOf(ReglaNegocioException.class);
    }
    @Test void cantidadNegativaNoModificaStock() {
        var p=producto(5);assertThatThrownBy(()->servicio.registrar(form(p.getId(),-2),"admin")).isInstanceOf(ReglaNegocioException.class);
        assertThat(productos.findById(p.getId()).orElseThrow().getStock()).isEqualTo(5);
    }
    @Test void fechaFinalIncluyeVentasDelDiaYFiltraCliente() {
        var p=producto(5);Long id=servicio.registrar(form(p.getId(),1),"admin");var hoy=ConsultaService.hoy();
        assertThat(consultas.ventas(hoy,hoy,1L)).extracting(Venta::getId).contains(id);
        assertThat(consultas.ventas(hoy,hoy,2L)).extracting(Venta::getId).doesNotContain(id);
        assertThatThrownBy(()->consultas.ventas(hoy.plusDays(1),hoy,null)).isInstanceOf(ReglaNegocioException.class);
    }
    @Test void accesoSinSesionRedirigeAlLogin() throws Exception {
        mvc.perform(get("/productos")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrlPattern("**/login"));
    }
    @Test void vendedorTienePermisosLimitadosEnServidor() throws Exception {
        mvc.perform(get("/productos").with(user("vendedor").roles("VENDEDOR"))).andExpect(status().isForbidden());
        mvc.perform(get("/reportes/inventario.pdf").with(user("vendedor").roles("VENDEDOR"))).andExpect(status().isForbidden());
        mvc.perform(post("/categorias/guardar").with(user("vendedor").roles("VENDEDOR")).with(csrf()).param("nombre","Prueba")).andExpect(status().isForbidden());
        mvc.perform(get("/consultas/inventario").with(user("vendedor").roles("VENDEDOR"))).andExpect(status().isOk());
    }
    @Test void csrfBloqueaMutacionSinToken() throws Exception {
        mvc.perform(post("/categorias/guardar").with(user("admin").roles("ADMIN")).param("nombre","Prueba")).andExpect(status().isForbidden());
    }
    @Test void loginRealYLogoutInvalidanSesion() throws Exception {
        var resultado=mvc.perform(formLogin().user("admin").password("AdminDemo2026!")).andExpect(authenticated().withUsername("admin")).andReturn();
        var session=(MockHttpSession)resultado.getRequest().getSession(false);assertThat(session).isNotNull();
        mvc.perform(get("/").session(session)).andExpect(status().isOk());
        mvc.perform(post("/logout").session(session).with(csrf())).andExpect(unauthenticated()).andExpect(redirectedUrl("/login?logout"));
        assertThat(session.isInvalid()).isTrue();
    }
    @Test void pantallasPrincipalesRenderizanConDatosReales() throws Exception {
        for(String url:new String[]{"/","/categorias","/categorias/nuevo","/categorias/1/editar","/clientes","/clientes/nuevo","/clientes/1/editar",
           "/productos","/productos/nuevo","/productos/1/editar","/consultas/ventas","/consultas/inventario","/ventas/nueva"}) {
            mvc.perform(get(url).with(user("admin").roles("ADMIN"))).andExpect(status().isOk());
        }
        mvc.perform(get("/login")).andExpect(status().isOk());
    }
    @Test void mantenimientosValidanDuplicadosYBajaLogica() throws Exception {
        long antes=categorias.count();
        mvc.perform(post("/categorias/guardar").with(user("admin").roles("ADMIN")).with(csrf()).param("nombre","Abarrotes").param("activo","true"))
           .andExpect(status().isOk()).andExpect(model().attributeHasErrors("form"));assertThat(categorias.count()).isEqualTo(antes);
        var f=new CategoriaForm();f.setNombre("Categoría "+UUID.randomUUID());catalogo.guardarCategoria(null,f);
        var c=categorias.findAll().stream().filter(x->x.getNombre().equals(f.getNombre())).findFirst().orElseThrow();
        catalogo.desactivarCategoria(c.getId());assertThat(categorias.findById(c.getId()).orElseThrow().isActivo()).isFalse();
        mvc.perform(post("/clientes/guardar").with(user("admin").roles("ADMIN")).with(csrf()).param("documento","123").param("nombre","Cliente inválido"))
            .andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("form","documento"));
    }
    @Test void ventaPorFormularioUsaUsuarioDeLaSesion() throws Exception {
        var p=producto(3);var session=new MockHttpSession();String token=UUID.randomUUID().toString();session.setAttribute("ventaToken",token);
        var r=mvc.perform(post("/ventas/registrar").with(user("vendedor").roles("VENDEDOR")).with(csrf()).session(session)
            .param("clienteId","1").param("token",token).param("lineas[0].productoId",p.getId().toString()).param("lineas[0].cantidad","2"))
            .andExpect(status().is3xxRedirection()).andReturn();
        String url=r.getResponse().getRedirectedUrl();
        mvc.perform(get(url).with(user("admin").roles("ADMIN"))).andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("vendedor")));
        assertThat(productos.findById(p.getId()).orElseThrow().getStock()).isEqualTo(1);
    }
    @Test void reportesGeneranPdfConYSinResultadosYExportanJrxml() throws Exception {
        var dir=Path.of("target","evidencias");Files.createDirectories(dir);
        byte[] inventario=reportes.inventario(null,"",false);
        byte[] venta=reportes.ventas(ConsultaService.hoy().minusDays(30),ConsultaService.hoy(),null);
        byte[] vacio=reportes.inventario(null,"NO_EXISTE_ABC",false);
        assertThat(new String(inventario,0,4,java.nio.charset.StandardCharsets.US_ASCII)).isEqualTo("%PDF");
        assertThat(inventario.length).isGreaterThan(1000);assertThat(venta.length).isGreaterThan(1000);assertThat(vacio.length).isGreaterThan(1000);
        Files.write(dir.resolve("inventario.pdf"),inventario);Files.write(dir.resolve("ventas.pdf"),venta);Files.write(dir.resolve("inventario_sin_resultados.pdf"),vacio);
        JRXmlWriter.writeReport(ReporteService.disenoInventario(),dir.resolve("inventario.jrxml").toString(),"UTF-8");
        JRXmlWriter.writeReport(ReporteService.disenoVentas(),dir.resolve("ventas.jrxml").toString(),"UTF-8");
        mvc.perform(get("/reportes/inventario.pdf").with(user("admin").roles("ADMIN"))).andExpect(status().isOk()).andExpect(content().contentType("application/pdf"));
    }
}
