package pe.idat.edualerta.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.idat.edualerta.Entity.Padre;
import pe.idat.edualerta.Repository.PadreRepository;
import pe.idat.edualerta.Entity.Alumno;
import pe.idat.edualerta.Entity.Notificacion;
import pe.idat.edualerta.Repository.NotificacionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PadreService {

    private final PadreRepository repository;
    private final NotificacionRepository notificacionRepository;

    // Listar todos los padres
    public List<Padre> listar() {
        return repository.findAll();
    }

    // Guardar un padre
    public Padre guardar(Padre padre) {
        return repository.save(padre);
    }

    // Actualizar un padre
    public Padre actualizar(Long id, Padre padre) {
        Padre existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Padre no encontrado"));

        existente.setNombres(padre.getNombres());
        existente.setApellidos(padre.getApellidos());
        existente.setDni(padre.getDni());
        if (padre.getAlumno() != null) {
            existente.setAlumno(padre.getAlumno());
        }
        if (padre.getEstado() != null) {
            existente.setEstado(padre.getEstado());
        }

        return repository.save(existente);
    }

    // Eliminar un padre
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    // Obtener alumno vinculado a un padre
    public Alumno obtenerAlumnoPorPadre(Long usuarioId) {
        Optional<Padre> padre = repository.findByUsuario_Id(usuarioId);
        return padre.orElseThrow(() -> new RuntimeException("Padre no encontrado"))
                     .getAlumno();
    }

    // Buscar padre por alumno
    public Padre buscarPorAlumno(Long alumnoId) {
        return repository.findByAlumnoId(alumnoId);
    }

    // Obtener notificaciones de un padre
    public List<Notificacion> obtenerNotificacionesPorPadre(Long padreId) {
        return notificacionRepository.findByPadreIdOrderByFechaDesc(padreId);
    }
}