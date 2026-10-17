package pe.edu.cibertec.minimarket.web.form;
import jakarta.validation.constraints.*;
public class ClienteForm {
    @NotBlank @Pattern(regexp="[0-9]{8}|[0-9]{11}", message="Usa 8 dígitos para DNI o 11 para RUC")
    private String documento;
    @NotBlank @Size(max=120)
    private String nombre;
    @Size(max=20)
    private String telefono;
    @Email(message="El correo no es válido") @Size(max=120)
    private String correo;

    private boolean activo = true;
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento=documento; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre=nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono=telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo=correo; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo=activo; }
}
