package pe.edu.cibertec.minimarket.web;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PessimisticLockingFailureException;
import pe.edu.cibertec.minimarket.web.form.VentaForm;
import pe.edu.cibertec.minimarket.repository.*;
import pe.edu.cibertec.minimarket.service.*;
@Controller
@RequestMapping("/ventas")
public class VentaController {
    private final VentaService servicio;
    private final VentaRepository ventas;
    private final ClienteRepository clientes;
    private final ProductoRepository productos;
    public VentaController(VentaService s,VentaRepository v,ClienteRepository c,ProductoRepository p){servicio=s;ventas=v;clientes=c;productos=p;}
    private String preparar(Model m){
        m.addAttribute("clientes",clientes.findByActivoTrueOrderByNombreAsc());
        m.addAttribute("productos",productos.findByActivoTrueOrderByNombreAsc().stream().filter(p->p.getCategoria().isActivo()).toList());return "ventas/form";
    }
    @GetMapping("/nueva") public String nueva(Model m,HttpSession session){
        var f=new VentaForm();f.setToken(UUID.randomUUID().toString());f.getLineas().add(new VentaForm.Linea());
        session.setAttribute("ventaToken",f.getToken());m.addAttribute("form",f);return preparar(m);
    }
    @PostMapping("/registrar") public String registrar(@Valid @ModelAttribute("form") VentaForm f,BindingResult r,Model m,HttpSession session,Principal principal){
        if(!java.util.Objects.equals(session.getAttribute("ventaToken"),f.getToken())) r.reject("token","Esta venta venció o ya fue procesada. Abre Nueva venta.");
        if(r.hasErrors()) return preparar(m);
        try {
            Long id=servicio.registrar(f,principal.getName());session.removeAttribute("ventaToken");return "redirect:/ventas/"+id;
        }catch(ReglaNegocioException e){r.reject("negocio",e.getMessage());}
        catch(DataIntegrityViolationException e){r.reject("duplicado","Esta venta ya fue procesada o los datos cambiaron. Revisa la consulta de ventas.");}
        catch(PessimisticLockingFailureException e){r.reject("stock","Otro usuario está actualizando el stock. Intenta nuevamente.");}
        return preparar(m);
    }
    @GetMapping("/{id}") public String detalle(@PathVariable Long id,Model m){
        m.addAttribute("venta",ventas.buscarDetalle(id).orElseThrow(()->new ReglaNegocioException("Venta no encontrada")));return "ventas/detalle";
    }
}
