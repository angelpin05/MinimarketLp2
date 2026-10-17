package pe.edu.cibertec.minimarket.model;
import jakarta.persistence.*;

@Entity
@Table(name="categoria")
public class Categoria {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true, length=80)
    private String nombre;
    @Column(length=200)
    private String descripcion;
    @Column(nullable=false)
    private boolean activo = true;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
