package pe.edu.cibertec.minimarket.model;
import jakarta.persistence.*;

@Entity
@Table(name="producto")
public class Producto {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true, length=30)
    private String codigo;
    @Column(nullable=false, length=120)
    private String nombre;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="categoria_id", nullable=false)
    private Categoria categoria;
    @Column(nullable=false, precision=10, scale=2)
    private java.math.BigDecimal precio;
    @Column(nullable=false)
    private int stock;
    @Column(nullable=false)
    private int stockMinimo;
    @Column(nullable=false)
    private boolean activo = true;
    @Version
    private Long version;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public java.math.BigDecimal getPrecio() { return precio; }
    public void setPrecio(java.math.BigDecimal precio) { this.precio = precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
