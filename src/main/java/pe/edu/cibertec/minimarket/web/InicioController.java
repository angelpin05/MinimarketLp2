package pe.edu.cibertec.minimarket.web;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pe.edu.cibertec.minimarket.repository.*;
@Controller
public class InicioController {
    private final ProductoRepository productos;
    private final ClienteRepository clientes;
    private final VentaRepository ventas;
    public InicioController(ProductoRepository p,ClienteRepository c,VentaRepository v){productos=p;clientes=c;ventas=v;}
    @GetMapping("/login") public String login(){return "login";}
    @GetMapping("/") public String inicio(Model m){
        m.addAttribute("productos",productos.count());m.addAttribute("clientes",clientes.count());m.addAttribute("ventas",ventas.count());
        m.addAttribute("bajoStock",productos.consultar(null,"",true).size());return "inicio";
    }
}
