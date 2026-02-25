package pe.idat.edualerta.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.idat.edualerta.Entity.Notificacion;
import pe.idat.edualerta.Repository.NotificacionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public Notificacion guardar(Notificacion n) {
        return notificacionRepository.save(n);
    }

    public List<Notificacion> obtenerPorPadreId(Long padreId) {
        return notificacionRepository.findByPadreIdOrderByFechaDesc(padreId);
    }

}