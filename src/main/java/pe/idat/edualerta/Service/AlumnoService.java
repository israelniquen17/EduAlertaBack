package pe.idat.edualerta.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.idat.edualerta.Entity.Alumno;
import pe.idat.edualerta.Repository.AlumnoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    private final AlumnoRepository repository;

    public List<Alumno> listar() {
        return repository.findAll();
    }

    public Alumno guardar(Alumno alumno) {
        alumno.setCodigoQr(alumno.getDni());
        if (alumno.getEstado() == null) {
            alumno.setEstado(Alumno.Estado.ACTIVO);
        }
        return repository.save(alumno);
    }

    public Alumno actualizar(Long id, Alumno alumno) {
        Alumno existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        existente.setNombres(alumno.getNombres());
        existente.setApellidos(alumno.getApellidos());
        existente.setGrado(alumno.getGrado());
        existente.setSeccion(alumno.getSeccion());
        existente.setDni(alumno.getDni());
        existente.setCodigoQr(alumno.getDni());
        if (alumno.getEstado() != null) {
            existente.setEstado(alumno.getEstado());
        }

        return repository.save(existente);
    }

    public Alumno buscarPorDni(String dni) {
        return repository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
    }

    public Alumno buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}