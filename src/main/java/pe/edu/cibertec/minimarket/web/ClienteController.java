package pe.edu.cibertec.minimarket.web;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import pe.edu.cibertec.minimarket.repository.ClienteRepository;

import pe.edu.cibertec.minimarket.web.form.ClienteForm;
import pe.edu.cibertec.minimarket.service.*;
@Controller
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteRepository repo;
    private final CatalogoService servicio;

    public ClienteController(ClienteRepository repo,CatalogoService servicio) {this.repo=repo;this.servicio=servicio;}
    @GetMapping public String listar(Model m){m.addAttribute("filas",repo.findAllByOrderByNombreAsc());return "clientes/lista";}
    @GetMapping("/nuevo") public String nuevo(Model m){m.addAttribute("form",new ClienteForm());return preparar(null,m);}
    @GetMapping("/{id}/editar") public String editar(@PathVariable Long id,Model m){
        var e=repo.findById(id).orElseThrow(()->new ReglaNegocioException("Registro no encontrado"));
        var f=new ClienteForm();BeanUtils.copyProperties(e,f);m.addAttribute("form",f);return preparar(id,m);
    }
    private String preparar(Long id,Model m){m.addAttribute("id",id);return "clientes/form";}
    @PostMapping({"/guardar","/{id}/guardar"})
    public String guardar(@PathVariable(required=false) Long id,@Valid @ModelAttribute("form") ClienteForm f,BindingResult r,Model m,RedirectAttributes a){
        if(r.hasErrors()) return preparar(id,m);
        try {servicio.guardarCliente(id,f);}
        catch(ReglaNegocioException e){r.reject("negocio",e.getMessage());return preparar(id,m);}
        catch(DataIntegrityViolationException e){r.reject("duplicado","Ya existe un registro con ese identificador");return preparar(id,m);}
        a.addFlashAttribute("mensaje","Registro guardado");return "redirect:/clientes";
    }
    @PostMapping("/{id}/desactivar") public String desactivar(@PathVariable Long id,RedirectAttributes a){
        servicio.desactivarCliente(id);a.addFlashAttribute("mensaje","Registro desactivado. Puedes reactivarlo desde Editar.");return "redirect:/clientes";
    }
}
