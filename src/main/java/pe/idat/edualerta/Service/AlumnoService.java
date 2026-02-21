package pe.idat.edualerta.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.idat.edualerta.Entity.Alumno;
import pe.idat.edualerta.Repository.AlumnoRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    private final AlumnoRepository repository;

    public List<Alumno> listar() {
        return repository.findAll();
    }

    public Alumno guardar(Alumno alumno) {
    // Generar QR automático
    alumno.setCodigoQr("QR-" + UUID.randomUUID());

    // 🔹 Si no viene estado, lo asignamos por defecto
    if (alumno.getEstado() == null) {
        alumno.setEstado(Alumno.Estado.ACTIVO);
    }

    return repository.save(alumno); // 🔹 guarda y persiste
}

    public Alumno actualizar(Long id, Alumno alumno) {
    Alumno existente = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

    existente.setNombres(alumno.getNombres());
    existente.setApellidos(alumno.getApellidos());
    existente.setGrado(alumno.getGrado());
    existente.setSeccion(alumno.getSeccion());
    existente.setDni(alumno.getDni());

    // 🔹 Actualizar estado solo si viene en la solicitud
    if (alumno.getEstado() != null) {
        existente.setEstado(alumno.getEstado());
    }

    return repository.save(existente);
}

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}