package pe.idat.edualerta.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.idat.edualerta.Entity.Curso;

import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    Optional<Curso> findByNombreAndNivel_IdAndGradoAndSeccion(String nombre, Long nivelId, String grado, String seccion);
}