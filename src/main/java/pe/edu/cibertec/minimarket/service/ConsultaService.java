package pe.edu.cibertec.minimarket.service;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.minimarket.model.Venta;
import pe.edu.cibertec.minimarket.repository.VentaRepository;
@Service
public class ConsultaService {
    private final VentaRepository ventas;
    public ConsultaService(VentaRepository ventas){this.ventas=ventas;}
    @Transactional(readOnly=true)
    public List<Venta> ventas(LocalDate desde,LocalDate hasta,Long cliente){
        if(desde==null || hasta==null || desde.isAfter(hasta)) throw new ReglaNegocioException("Ingresa un rango de fechas válido");
        if(java.time.temporal.ChronoUnit.DAYS.between(desde,hasta)>3660) throw new ReglaNegocioException("El rango debe ser de 10 años o menos");
        return ventas.consultar(desde.atStartOfDay(),hasta.plusDays(1).atStartOfDay(),cliente);
    }
    public static LocalDate hoy(){return LocalDate.now(ZoneId.of("America/Lima"));}
}
