package pe.edu.cibertec.minimarket.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.core.env.Environment;
import pe.edu.cibertec.minimarket.model.Usuario;
import pe.edu.cibertec.minimarket.repository.UsuarioRepository;

@Component
public class DatosIniciales implements CommandLineRunner {
    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final Environment env;
    public DatosIniciales(UsuarioRepository usuarios, PasswordEncoder encoder, Environment env) {
        this.usuarios=usuarios; this.encoder=encoder; this.env=env;
    }
    @Override @Transactional public void run(String... args) {
        if (usuarios.count() > 0) return;
        boolean demo = env.matchesProfiles("demo", "test");
        String admin=env.getProperty("app.bootstrap.admin-password", "");
        String vendedor=env.getProperty("app.bootstrap.vendedor-password", "");
        if (demo) { admin="AdminDemo2026!"; vendedor="VentaDemo2026!"; }
        if (admin.length()<12 || vendedor.length()<12 || admin.equals(vendedor)) {
            throw new IllegalStateException("Define ADMIN_PASSWORD y VENDEDOR_PASSWORD distintos, de al menos 12 caracteres, antes del primer inicio.");
        }
        crear("admin", "Administrador", "ADMIN", admin);
        crear("vendedor", "Vendedor", "VENDEDOR", vendedor);
    }
    private void crear(String username, String nombre, String rol, String password) {
        var u=new Usuario(); u.setUsername(username); u.setNombre(nombre); u.setRol(rol); u.setPassword(encoder.encode(password));
        usuarios.save(u);
    }
}
