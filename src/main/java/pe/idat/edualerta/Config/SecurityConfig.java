package pe.idat.edualerta.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    // 🔹 Bean para encriptar contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔹 Configuración de seguridad HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF (útil para APIs)
            .csrf(csrf -> csrf.disable())
            // Habilitar CORS con la configuración definida abajo
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Permitir todos los endpoints durante desarrollo
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // ⚡ Todo libre por ahora
            )
            // Deshabilitar HTTP Basic
            .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }

    // 🔹 Configuración de CORS para Angular
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permitir solicitudes desde Angular dev server
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplicar configuración a todos los endpoints
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}