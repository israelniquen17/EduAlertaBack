package pe.idat.edualerta.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.idat.edualerta.Entity.Padre;

import java.util.List;
import java.util.Optional;

public interface PadreRepository extends JpaRepository<Padre, Long> {

    Optional<Padre> findByUsuario_Id(Long usuarioId);

    Padre findByAlumnoId(Long alumnoId);

    List<Padre> findAllByAlumnoId(Long alumnoId);

    boolean existsByDni(String dni);
    boolean existsByTelefono(String telefono);
}