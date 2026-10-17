package pe.edu.cibertec.minimarket.web.form;
import jakarta.validation.constraints.*;
public class CategoriaForm {
    @NotBlank(message="Ingresa el nombre") @Size(max=80)
    private String nombre;
    @Size(max=200)
    private String descripcion;

    private boolean activo = true;
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre=nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion=descripcion; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo=activo; }
}
