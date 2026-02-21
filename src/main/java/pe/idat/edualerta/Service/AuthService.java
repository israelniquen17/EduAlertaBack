package pe.idat.edualerta.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.idat.edualerta.Entity.Usuario;
import pe.idat.edualerta.Repository.UsuarioRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    public Usuario authenticate(String usuario, String password) throws Exception {
        Usuario user = usuarioRepository.findByUsuario(usuario)
                .orElseThrow(() -> new Exception("Usuario o contraseña incorrecta"));

        if (!user.getEstado().equals(Usuario.Estado.ACTIVO)) {
            throw new Exception("Usuario inactivo");
        }

        if (!user.getPassword().equals(password)) { // 🔹 texto plano por ahora
            throw new Exception("Usuario o contraseña incorrecta");
        }

        return user;
    }
}