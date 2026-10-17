package pe.edu.cibertec.minimarket.web.form;
import jakarta.validation.constraints.*;
public class ProductoForm {
    private Long version;
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version=version; }
    @NotBlank @Pattern(regexp="[A-Za-z0-9_-]{1,30}", message="Código alfanumérico de hasta 30 caracteres")
    private String codigo;
    @NotBlank @Size(max=120)
    private String nombre;
    @NotNull(message="Selecciona una categoría")
    private Long categoriaId;
    @NotNull @DecimalMin(value="0.01") @Digits(integer=8,fraction=2)
    private java.math.BigDecimal precio;
    @NotNull @Min(0)
    private Integer stock = 0;
    @NotNull @Min(0)
    private Integer stockMinimo = 5;

    private boolean activo = true;
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo=codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre=nombre; }
    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId=categoriaId; }
    public java.math.BigDecimal getPrecio() { return precio; }
    public void setPrecio(java.math.BigDecimal precio) { this.precio=precio; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock=stock; }
    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo=stockMinimo; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo=activo; }
}
