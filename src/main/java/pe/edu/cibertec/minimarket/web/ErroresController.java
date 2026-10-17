package pe.edu.cibertec.minimarket.web;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pe.edu.cibertec.minimarket.service.ReglaNegocioException;
@ControllerAdvice
public class ErroresController {
    @ExceptionHandler({ReglaNegocioException.class,MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String datosInvalidos(Exception e,Model m){
        m.addAttribute("mensajeError",e instanceof ReglaNegocioException?e.getMessage():"Revisa el formato de los filtros o identificadores");return "error/400";
    }
}
