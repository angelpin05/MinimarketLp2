package pe.edu.cibertec.minimarket.repository;
import pe.edu.cibertec.minimarket.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {

java.util.List<Categoria> findAllByOrderByNombreAsc();
java.util.List<Categoria> findByActivoTrueOrderByNombreAsc();
boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);

}
