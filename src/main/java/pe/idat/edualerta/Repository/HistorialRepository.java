package pe.idat.edualerta.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.idat.edualerta.Entity.HistorialQr;

@Repository
public interface HistorialRepository extends JpaRepository<HistorialQr, Long> {
    // Aquí podrías agregar búsquedas por alumno si quieres
}