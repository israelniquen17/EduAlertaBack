package pe.idat.edualerta.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.idat.edualerta.Entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsuario(String usuario);
}