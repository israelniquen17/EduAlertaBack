package pe.idat.edualerta.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pe.idat.edualerta.Entity.Docente;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {

    Optional<Docente> findByDni(String dni);

    // 🔥 ESTO ES LO QUE FALTABA
    @Query("SELECT DISTINCT d FROM Docente d LEFT JOIN FETCH d.cursos")
    List<Docente> findAllConCursos();
}