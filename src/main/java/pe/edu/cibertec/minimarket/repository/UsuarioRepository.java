package pe.edu.cibertec.minimarket.repository;
import pe.edu.cibertec.minimarket.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

java.util.Optional<Usuario> findByUsername(String username);

}
