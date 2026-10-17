package pe.edu.cibertec.minimarket.model;
import jakarta.persistence.*;

@Entity
@Table(name="usuario")
public class Usuario {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true, length=50)
    private String username;
    @Column(nullable=false, length=120)
    private String nombre;
    @Column(nullable=false, length=100)
    private String password;
    @Column(nullable=false, length=20)
    private String rol;
    @Column(nullable=false)
    private boolean activo = true;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
