package pe.idat.edualerta.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.idat.edualerta.Entity.Usuario;
import pe.idat.edualerta.Repository.UsuarioRepository;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 🔹 Al iniciar la aplicación:
     * - Encripta cualquier contraseña en texto plano
     * - Crea usuario admin si no existe
     */
    @PostConstruct
    public void init() {

        List<Usuario> usuarios = usuarioRepository.findAll();

        for (Usuario u : usuarios) {

            if (u.getPassword() == null || u.getPassword().isBlank()) {
                continue;
            }

            if (!isBCryptHash(u.getPassword())) {
                u.setPassword(passwordEncoder.encode(u.getPassword()));
                usuarioRepository.save(u);
            }
        }

        // Crear admin si no existe
        if (usuarioRepository.findByUsuario("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsuario("admin");
            admin.setPassword(passwordEncoder.encode("1234"));
            admin.setRol(Usuario.Rol.ADMIN);
            admin.setEstado(Usuario.Estado.ACTIVO);
            usuarioRepository.save(admin);
        }
    }

    /**
     * 🔹 LOGIN
     */
    public Usuario authenticate(String usuario, String password) {

        Optional<Usuario> optionalUser = usuarioRepository.findByUsuario(usuario);

        if (optionalUser.isEmpty()) {
            return null;
        }

        Usuario user = optionalUser.get();

        if (user.getEstado() != Usuario.Estado.ACTIVO) {
            return null;
        }

        String storedPassword = user.getPassword();

        if (storedPassword == null || storedPassword.isBlank()) {
            return null;
        }

        // 🔥 Si la contraseña está en texto plano (caso BD manual)
        if (!isBCryptHash(storedPassword)) {

            // Si coincide en texto plano
            if (storedPassword.equals(password)) {

                // 🔐 Convertimos automáticamente a BCrypt
                user.setPassword(passwordEncoder.encode(password));
                usuarioRepository.save(user);

                return user;
            }

            return null;
        }

        // 🔐 Caso normal BCrypt
        if (passwordEncoder.matches(password, storedPassword)) {
            return user;
        }

        return null;
    }

    /**
     * 🔹 REGISTRO
     */
    public Usuario saveUser(Usuario user) {

        if (usuarioRepository.findByUsuario(user.getUsuario()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEstado(Usuario.Estado.ACTIVO);

        return usuarioRepository.save(user);
    }

    /**
     * 🔹 ACTUALIZAR CONTRASEÑA
     */
    public Usuario updatePassword(Long userId, String newPassword) {

        Usuario user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setPassword(passwordEncoder.encode(newPassword));

        return usuarioRepository.save(user);
    }

    /**
     * 🔹 Detecta si es hash BCrypt
     */
    private boolean isBCryptHash(String password) {
        return password.startsWith("$2a$") ||
               password.startsWith("$2b$") ||
               password.startsWith("$2y$");
    }
}