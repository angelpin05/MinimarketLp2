package pe.edu.cibertec.minimarket.repository;
import pe.edu.cibertec.minimarket.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
public interface VentaRepository extends JpaRepository<Venta,Long> {

boolean existsByToken(String token);
@org.springframework.data.jpa.repository.EntityGraph(attributePaths={"cliente","usuario"})
@org.springframework.data.jpa.repository.Query("select v from Venta v where v.fecha >= :desde and v.fecha < :hasta "
  + "and (:cliente is null or v.cliente.id=:cliente) order by v.fecha desc, v.id desc")
java.util.List<Venta> consultar(@org.springframework.data.repository.query.Param("desde") java.time.LocalDateTime desde,
  @org.springframework.data.repository.query.Param("hasta") java.time.LocalDateTime hasta,
  @org.springframework.data.repository.query.Param("cliente") Long cliente);
@org.springframework.data.jpa.repository.EntityGraph(attributePaths={"cliente","usuario","detalles","detalles.producto"})
@org.springframework.data.jpa.repository.Query("select v from Venta v where v.id=:id")
java.util.Optional<Venta> buscarDetalle(@org.springframework.data.repository.query.Param("id") Long id);

}
