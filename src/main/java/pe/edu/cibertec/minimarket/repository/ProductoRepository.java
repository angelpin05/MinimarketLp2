package pe.edu.cibertec.minimarket.repository;
import pe.edu.cibertec.minimarket.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductoRepository extends JpaRepository<Producto,Long> {

@org.springframework.data.jpa.repository.EntityGraph(attributePaths="categoria")
java.util.List<Producto> findAllByOrderByNombreAsc();
@org.springframework.data.jpa.repository.EntityGraph(attributePaths="categoria")
java.util.List<Producto> findByActivoTrueOrderByNombreAsc();
boolean existsByCodigoIgnoreCaseAndIdNot(String codigo, Long id);
@org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
@org.springframework.data.jpa.repository.Query("select p from Producto p where p.id=:id")
java.util.Optional<Producto> bloquearPorId(@org.springframework.data.repository.query.Param("id") Long id);
@org.springframework.data.jpa.repository.EntityGraph(attributePaths="categoria")
@org.springframework.data.jpa.repository.Query("select p from Producto p where p.activo=true "
  + "and (:categoria is null or p.categoria.id=:categoria) "
  + "and (lower(p.nombre) like lower(concat('%',:texto,'%')) or lower(p.codigo) like lower(concat('%',:texto,'%'))) "
  + "and (:bajo=false or p.stock <= p.stockMinimo) order by p.nombre")
java.util.List<Producto> consultar(@org.springframework.data.repository.query.Param("categoria") Long categoria,
  @org.springframework.data.repository.query.Param("texto") String texto, @org.springframework.data.repository.query.Param("bajo") boolean bajo);

}
