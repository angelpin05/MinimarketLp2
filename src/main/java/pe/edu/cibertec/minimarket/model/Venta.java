package pe.edu.cibertec.minimarket.model;
import jakarta.persistence.*;

@Entity
@Table(name="venta")
public class Venta {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private java.time.LocalDateTime fecha;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="cliente_id", nullable=false)
    private Cliente cliente;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="usuario_id", nullable=false)
    private Usuario usuario;
    @Column(nullable=false, precision=12, scale=2)
    private java.math.BigDecimal total;
    @Column(nullable=false, unique=true, length=36)
    private String token;
    @OneToMany(mappedBy="venta", cascade=CascadeType.ALL, orphanRemoval=true)
    private java.util.List<DetalleVenta> detalles = new java.util.ArrayList<>();
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public java.time.LocalDateTime getFecha() { return fecha; }
    public void setFecha(java.time.LocalDateTime fecha) { this.fecha = fecha; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public java.math.BigDecimal getTotal() { return total; }
    public void setTotal(java.math.BigDecimal total) { this.total = total; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public java.util.List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(java.util.List<DetalleVenta> detalles) { this.detalles = detalles; }
}
