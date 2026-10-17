package pe.edu.cibertec.minimarket.web;
import java.time.LocalDate;
import java.math.BigDecimal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.minimarket.repository.*;
import pe.edu.cibertec.minimarket.service.ConsultaService;
@Controller
@RequestMapping("/consultas")
public class ConsultaController {
    private final ConsultaService consultas;
    private final CategoriaRepository categorias;
    private final ProductoRepository productos;
    private final ClienteRepository clientes;
    public ConsultaController(ConsultaService s,CategoriaRepository c,ProductoRepository p,ClienteRepository cl){consultas=s;categorias=c;productos=p;clientes=cl;}
    @GetMapping("/inventario") public String inventario(@RequestParam(required=false) Long categoria,@RequestParam(defaultValue="") String texto,
      @RequestParam(defaultValue="false") boolean bajo,Model m){
        m.addAttribute("filas",productos.consultar(categoria,texto,bajo));m.addAttribute("categorias",categorias.findAllByOrderByNombreAsc());
        m.addAttribute("categoria",categoria);m.addAttribute("texto",texto);m.addAttribute("bajo",bajo);return "consultas/inventario";
    }
    @GetMapping("/ventas") public String ventas(@RequestParam(required=false) LocalDate desde,@RequestParam(required=false) LocalDate hasta,
      @RequestParam(required=false) Long cliente,Model m){
        if(desde==null) desde=ConsultaService.hoy().withDayOfMonth(1);if(hasta==null) hasta=ConsultaService.hoy();
        var filas=consultas.ventas(desde,hasta,cliente);
        m.addAttribute("filas",filas);m.addAttribute("total",filas.stream().map(v->v.getTotal()).reduce(BigDecimal.ZERO,BigDecimal::add));
        m.addAttribute("clientes",clientes.findAllByOrderByNombreAsc());m.addAttribute("desde",desde);m.addAttribute("hasta",hasta);m.addAttribute("cliente",cliente);
        return "consultas/ventas";
    }
}
