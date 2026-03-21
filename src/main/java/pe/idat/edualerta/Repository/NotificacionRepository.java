package pe.idat.edualerta.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.idat.edualerta.Entity.Notificacion;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByPadreIdOrderByFechaDesc(Long padreId);

    List<Notificacion> findByPadreIdAndLeidoFalseOrderByFechaDesc(Long padreId);
}