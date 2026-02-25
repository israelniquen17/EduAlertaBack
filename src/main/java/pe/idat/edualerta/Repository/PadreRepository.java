package pe.idat.edualerta.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.idat.edualerta.Entity.Padre;

import java.util.Optional;

public interface PadreRepository extends JpaRepository<Padre, Long> {

    // Buscar padre por el id del usuario asociado
    Optional<Padre> findByUsuario_Id(Long usuarioId);

    // Buscar padre por el id del alumno asociado
    Padre findByAlumnoId(Long alumnoId);
}