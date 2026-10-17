package pe.edu.cibertec.minimarket.web;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import net.sf.jasperreports.engine.JRException;
import pe.edu.cibertec.minimarket.service.*;
@Controller
@RequestMapping("/reportes")
public class ReporteController {
    private final ReporteService servicio;
    public ReporteController(ReporteService s){servicio=s;}
    @GetMapping("/inventario.pdf") public ResponseEntity<byte[]> inventario(@RequestParam(required=false) Long categoria,
      @RequestParam(defaultValue="") String texto,@RequestParam(defaultValue="false") boolean bajo) throws JRException {
        return archivo(servicio.inventario(categoria,texto,bajo),"inventario.pdf");
    }
    @GetMapping("/ventas.pdf") public ResponseEntity<byte[]> ventas(@RequestParam(required=false) LocalDate desde,
      @RequestParam(required=false) LocalDate hasta,@RequestParam(required=false) Long cliente) throws JRException {
        if(desde==null) desde=ConsultaService.hoy().withDayOfMonth(1);if(hasta==null) hasta=ConsultaService.hoy();
        return archivo(servicio.ventas(desde,hasta,cliente),"ventas.pdf");
    }
    private ResponseEntity<byte[]> archivo(byte[] pdf,String nombre){return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
        .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+nombre+"\"").cacheControl(CacheControl.noStore()).body(pdf);}
}
