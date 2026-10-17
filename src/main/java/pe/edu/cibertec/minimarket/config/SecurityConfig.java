package pe.edu.cibertec.minimarket.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pe.edu.cibertec.minimarket.repository.UsuarioRepository;

@Configuration
public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean UserDetailsService usuarios(UsuarioRepository repo) {
        return username -> {
            var u = repo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            return User.withUsername(u.getUsername()).password(u.getPassword()).roles(u.getRol()).disabled(!u.isActivo()).build();
        };
    }
    @Bean SecurityFilterChain seguridad(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(a -> a
            .requestMatchers("/login", "/css/**", "/js/**", "/actuator/health", "/error").permitAll()
            .requestMatchers("/categorias/**", "/productos/**", "/reportes/**").hasRole("ADMIN")
            .requestMatchers("/clientes/**", "/ventas/**", "/consultas/**", "/").hasAnyRole("ADMIN", "VENDEDOR")
            .anyRequest().authenticated())
            .formLogin(f -> f.loginPage("/login").defaultSuccessUrl("/",true).permitAll())
            .logout(l -> l.logoutSuccessUrl("/login?logout").invalidateHttpSession(true).deleteCookies("JSESSIONID"))
            .sessionManagement(s -> s.sessionFixation(f -> f.migrateSession()));
        // CSRF permanece activo. Thymeleaf incorpora el token en formularios POST.
        return http.build();
    }
}
