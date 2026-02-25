package pe.idat.edualerta.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.idat.edualerta.Entity.Usuario;
import pe.idat.edualerta.Service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    // 🔹 LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

            Usuario user = authService.authenticate(request.getUsuario(), request.getPassword());
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "Usuario o contraseña incorrecta"));
        }

        user.setPassword(null); // no enviamos la contraseña
        return ResponseEntity.ok(user);
    }

    // 🔹 REGISTRO DE USUARIO NUEVO
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            Usuario nuevo = new Usuario();
            nuevo.setUsuario(request.getUsuario());
            nuevo.setPassword(request.getPassword()); // texto plano
            nuevo.setRol(request.getRol());
            nuevo.setEstado(Usuario.Estado.ACTIVO);

            Usuario saved = authService.saveUser(nuevo);
            saved.setPassword(null); // no enviamos contraseña
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }

    // 🔹 DTO para login
    public static class LoginRequest {
        private String usuario;
        private String password;

        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // 🔹 DTO para registro
    public static class RegisterRequest {
        private String usuario;
        private String password;
        private Usuario.Rol rol;

        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public Usuario.Rol getRol() { return rol; }
        public void setRol(Usuario.Rol rol) { this.rol = rol; }
    }
}