package pe.edu.cibertec.minimarket.web;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import pe.edu.cibertec.minimarket.repository.ProductoRepository;
import pe.edu.cibertec.minimarket.repository.CategoriaRepository;
import pe.edu.cibertec.minimarket.web.form.ProductoForm;
import pe.edu.cibertec.minimarket.service.*;
@Controller
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoRepository repo;
    private final CatalogoService servicio;
    private final CategoriaRepository categorias;
    public ProductoController(ProductoRepository repo,CatalogoService servicio, CategoriaRepository categorias) {this.repo=repo;this.servicio=servicio;this.categorias=categorias;}
    @GetMapping public String listar(Model m){m.addAttribute("filas",repo.findAllByOrderByNombreAsc());return "productos/lista";}
    @GetMapping("/nuevo") public String nuevo(Model m){m.addAttribute("form",new ProductoForm());return preparar(null,m);}
    @GetMapping("/{id}/editar") public String editar(@PathVariable Long id,Model m){
        var e=repo.findById(id).orElseThrow(()->new ReglaNegocioException("Registro no encontrado"));
        var f=new ProductoForm();BeanUtils.copyProperties(e,f);f.setCategoriaId(e.getCategoria().getId());f.setVersion(e.getVersion());m.addAttribute("form",f);return preparar(id,m);
    }
    private String preparar(Long id,Model m){m.addAttribute("id",id);m.addAttribute("categorias",categorias.findAllByOrderByNombreAsc());return "productos/form";}
    @PostMapping({"/guardar","/{id}/guardar"})
    public String guardar(@PathVariable(required=false) Long id,@Valid @ModelAttribute("form") ProductoForm f,BindingResult r,Model m,RedirectAttributes a){
        if(r.hasErrors()) return preparar(id,m);
        try {servicio.guardarProducto(id,f);}
        catch(ReglaNegocioException e){r.reject("negocio",e.getMessage());return preparar(id,m);}
        catch(DataIntegrityViolationException e){r.reject("duplicado","Ya existe un registro con ese identificador");return preparar(id,m);}
        a.addFlashAttribute("mensaje","Registro guardado");return "redirect:/productos";
    }
    @PostMapping("/{id}/desactivar") public String desactivar(@PathVariable Long id,RedirectAttributes a){
        servicio.desactivarProducto(id);a.addFlashAttribute("mensaje","Registro desactivado. Puedes reactivarlo desde Editar.");return "redirect:/productos";
    }
}
