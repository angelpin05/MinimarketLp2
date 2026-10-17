package pe.edu.cibertec.minimarket.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.minimarket.model.*;
import pe.edu.cibertec.minimarket.repository.*;
import pe.edu.cibertec.minimarket.web.form.*;

@Service
public class CatalogoService {
    private final CategoriaRepository categorias;
    private final ProductoRepository productos;
    private final ClienteRepository clientes;
    public CatalogoService(CategoriaRepository c, ProductoRepository p, ClienteRepository cl) { categorias=c;productos=p;clientes=cl; }
    @Transactional public void guardarCategoria(Long id, CategoriaForm f) {
        var c=id==null?new Categoria():categorias.findById(id).orElseThrow(()->new ReglaNegocioException("Categoría no encontrada"));
        String nombre=f.getNombre().strip();
        if(categorias.existsByNombreIgnoreCaseAndIdNot(nombre,id==null?0L:id)) throw new ReglaNegocioException("Ya existe esa categoría");
        c.setNombre(nombre);c.setDescripcion(f.getDescripcion());c.setActivo(f.isActivo());categorias.save(c);
    }
    @Transactional public void guardarCliente(Long id, ClienteForm f) {
        var c=id==null?new Cliente():clientes.findById(id).orElseThrow(()->new ReglaNegocioException("Cliente no encontrado"));
        if(clientes.existsByDocumentoAndIdNot(f.getDocumento(),id==null?0L:id)) throw new ReglaNegocioException("Ya existe ese documento");
        c.setDocumento(f.getDocumento());c.setNombre(f.getNombre().strip());c.setTelefono(f.getTelefono());c.setCorreo(f.getCorreo());c.setActivo(f.isActivo());clientes.save(c);
    }
    @Transactional public void guardarProducto(Long id, ProductoForm f) {
        // Se bloquea el producto y se compara la versión enviada por el formulario.
        var p=id==null?new Producto():productos.bloquearPorId(id).orElseThrow(()->new ReglaNegocioException("Producto no encontrado"));
        if(id!=null && !java.util.Objects.equals(p.getVersion(),f.getVersion())) throw new ReglaNegocioException("El producto cambió mientras editabas. Recarga el formulario para ver el stock actual.");
        if(productos.existsByCodigoIgnoreCaseAndIdNot(f.getCodigo(),id==null?0L:id)) throw new ReglaNegocioException("Ya existe ese código");
        var c=categorias.findById(f.getCategoriaId()).orElseThrow(()->new ReglaNegocioException("Categoría no encontrada"));
        if(!c.isActivo() && (p.getCategoria()==null || !p.getCategoria().getId().equals(c.getId()))) throw new ReglaNegocioException("Selecciona una categoría activa");
        p.setCodigo(f.getCodigo().toUpperCase(java.util.Locale.ROOT));p.setNombre(f.getNombre().strip());p.setCategoria(c);
        p.setPrecio(f.getPrecio());p.setStock(f.getStock());p.setStockMinimo(f.getStockMinimo());p.setActivo(f.isActivo());productos.save(p);
    }
    @Transactional public void desactivarCategoria(Long id) {
        var c=categorias.findById(id).orElseThrow(()->new ReglaNegocioException("Categoría no encontrada"));c.setActivo(false);
    }
    @Transactional public void desactivarCliente(Long id) {
        var c=clientes.findById(id).orElseThrow(()->new ReglaNegocioException("Cliente no encontrado"));c.setActivo(false);
    }
    @Transactional public void desactivarProducto(Long id) {
        var p=productos.bloquearPorId(id).orElseThrow(()->new ReglaNegocioException("Producto no encontrado"));p.setActivo(false);
    }
}
