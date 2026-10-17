package pe.edu.cibertec.minimarket.model;
import jakarta.persistence.*;

@Entity
@Table(name="cliente")
public class Cliente {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true, length=12)
    private String documento;
    @Column(nullable=false, length=120)
    private String nombre;
    @Column(length=20)
    private String telefono;
    @Column(length=120)
    private String correo;
    @Column(nullable=false)
    private boolean activo = true;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
