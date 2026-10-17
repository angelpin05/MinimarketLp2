package pe.edu.cibertec.minimarket.model;
import jakarta.persistence.*;

@Entity
@Table(name="detalle_venta")
public class DetalleVenta {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="venta_id", nullable=false)
    private Venta venta;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="producto_id", nullable=false)
    private Producto producto;
    @Column(nullable=false, length=120)
    private String nombreProducto;
    @Column(nullable=false)
    private int cantidad;
    @Column(nullable=false, precision=10, scale=2)
    private java.math.BigDecimal precioUnitario;
    @Column(nullable=false, precision=12, scale=2)
    private java.math.BigDecimal subtotal;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public java.math.BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(java.math.BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public java.math.BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(java.math.BigDecimal subtotal) { this.subtotal = subtotal; }
}
