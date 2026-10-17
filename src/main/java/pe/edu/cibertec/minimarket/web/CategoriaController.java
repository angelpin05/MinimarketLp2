package pe.edu.cibertec.minimarket.web;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import pe.edu.cibertec.minimarket.repository.CategoriaRepository;

import pe.edu.cibertec.minimarket.web.form.CategoriaForm;
import pe.edu.cibertec.minimarket.service.*;
@Controller
@RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaRepository repo;
    private final CatalogoService servicio;

    public CategoriaController(CategoriaRepository repo,CatalogoService servicio) {this.repo=repo;this.servicio=servicio;}
    @GetMapping public String listar(Model m){m.addAttribute("filas",repo.findAllByOrderByNombreAsc());return "categorias/lista";}
    @GetMapping("/nuevo") public String nuevo(Model m){m.addAttribute("form",new CategoriaForm());return preparar(null,m);}
    @GetMapping("/{id}/editar") public String editar(@PathVariable Long id,Model m){
        var e=repo.findById(id).orElseThrow(()->new ReglaNegocioException("Registro no encontrado"));
        var f=new CategoriaForm();BeanUtils.copyProperties(e,f);m.addAttribute("form",f);return preparar(id,m);
    }
    private String preparar(Long id,Model m){m.addAttribute("id",id);return "categorias/form";}
    @PostMapping({"/guardar","/{id}/guardar"})
    public String guardar(@PathVariable(required=false) Long id,@Valid @ModelAttribute("form") CategoriaForm f,BindingResult r,Model m,RedirectAttributes a){
        if(r.hasErrors()) return preparar(id,m);
        try {servicio.guardarCategoria(id,f);}
        catch(ReglaNegocioException e){r.reject("negocio",e.getMessage());return preparar(id,m);}
        catch(DataIntegrityViolationException e){r.reject("duplicado","Ya existe un registro con ese identificador");return preparar(id,m);}
        a.addFlashAttribute("mensaje","Registro guardado");return "redirect:/categorias";
    }
    @PostMapping("/{id}/desactivar") public String desactivar(@PathVariable Long id,RedirectAttributes a){
        servicio.desactivarCategoria(id);a.addFlashAttribute("mensaje","Registro desactivado. Puedes reactivarlo desde Editar.");return "redirect:/categorias";
    }
}
