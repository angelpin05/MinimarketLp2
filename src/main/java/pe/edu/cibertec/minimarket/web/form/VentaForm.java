package pe.edu.cibertec.minimarket.web.form;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
public class VentaForm {
    @NotNull private Long clienteId;
    @NotBlank @Pattern(regexp="[0-9a-fA-F-]{36}") private String token;
    @Valid @NotEmpty @Size(max=30) private List<Linea> lineas=new ArrayList<>();
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long v) { clienteId=v; }
    public String getToken() { return token; }
    public void setToken(String v) { token=v; }
    public List<Linea> getLineas() { return lineas; }
    public void setLineas(List<Linea> v) { lineas=v; }
    public static class Linea {
        @NotNull private Long productoId;
        @NotNull @Min(1) @Max(100000) private Integer cantidad=1;
        public Long getProductoId() { return productoId; }
        public void setProductoId(Long v) { productoId=v; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer v) { cantidad=v; }
    }
}
