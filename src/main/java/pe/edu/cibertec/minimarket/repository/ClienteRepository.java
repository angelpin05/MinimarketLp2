package pe.edu.cibertec.minimarket.repository;
import pe.edu.cibertec.minimarket.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ClienteRepository extends JpaRepository<Cliente,Long> {

java.util.List<Cliente> findAllByOrderByNombreAsc();
java.util.List<Cliente> findByActivoTrueOrderByNombreAsc();
boolean existsByDocumentoAndIdNot(String documento, Long id);

}
