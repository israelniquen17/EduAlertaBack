package pe.idat.edualerta.Controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.idat.edualerta.Entity.Usuario;
import pe.idat.edualerta.Repository.UsuarioRepository;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Usuario request) {

    Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(request.getUsuario());

    if (usuarioOpt.isPresent()) {

        Usuario usuario = usuarioOpt.get();

        if (usuario.getPassword().equals(request.getPassword())
            && usuario.getEstado() == Usuario.Estado.ACTIVO) {

            return ResponseEntity.ok(usuario);
        }
    }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(Map.of("mensaje", "Usuario o contraseña incorrectos"));
}
}