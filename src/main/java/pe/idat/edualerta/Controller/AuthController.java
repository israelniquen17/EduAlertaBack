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

    private final AuthService authService; // 🔥 Inyectamos el AuthService

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Usuario user = authService.authenticate(request.getUsuario(), request.getPassword());
            user.setPassword(null); // no enviamos la contraseña
            return ResponseEntity.ok(user);

        } catch (Exception e) {
            // 401 Unauthorized para usuario o contraseña incorrecta
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "Usuario o contraseña incorrecta"));
        }
    }

    public static class LoginRequest {
        private String usuario;
        private String password;

        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}