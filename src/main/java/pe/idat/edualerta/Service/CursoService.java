package pe.idat.edualerta.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.idat.edualerta.Entity.Curso;
import pe.idat.edualerta.Entity.Nivel;
import pe.idat.edualerta.Repository.CursoRepository;
import pe.idat.edualerta.Repository.NivelRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository repository;
    private final NivelRepository nivelRepository;

    public List<Curso> listar() {
        return repository.findAll();
    }

    public Curso guardar(Curso curso) {
        validarCurso(curso);

        Nivel nivel = nivelRepository.findById(curso.getNivel().getId())
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));

        repository.findByNombreAndNivel_IdAndGradoAndSeccion(
                curso.getNombre().trim(),
                curso.getNivel().getId(),
                curso.getGrado().trim(),
                curso.getSeccion().trim()
        ).ifPresent(c -> {
            throw new RuntimeException("Ya existe ese curso para el mismo nivel, grado y sección");
        });

        curso.setNivel(nivel);
        return repository.save(curso);
    }

    public Curso actualizar(Long id, Curso curso) {
        validarCurso(curso);

        Curso existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Nivel nivel = nivelRepository.findById(curso.getNivel().getId())
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));

        repository.findByNombreAndNivel_IdAndGradoAndSeccion(
                curso.getNombre().trim(),
                curso.getNivel().getId(),
                curso.getGrado().trim(),
                curso.getSeccion().trim()
        ).ifPresent(c -> {
            if (!c.getId().equals(id)) {
                throw new RuntimeException("Ya existe ese curso para el mismo nivel, grado y sección");
            }
        });

        existente.setNombre(curso.getNombre().trim());
        existente.setNivel(nivel);
        existente.setGrado(curso.getGrado().trim());
        existente.setSeccion(curso.getSeccion().trim());

        if (curso.getEstado() != null) {
            existente.setEstado(curso.getEstado());
        }

        return repository.save(existente);
    }

    public void eliminar(Long id) {
        Curso curso = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        curso.getDocentes().clear();
        repository.save(curso);
        repository.delete(curso);
    }

    private void validarCurso(Curso curso) {
        if (curso.getNombre() == null || curso.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del curso es obligatorio");
        }

        if (curso.getNivel() == null || curso.getNivel().getId() == null) {
            throw new RuntimeException("El nivel es obligatorio");
        }

        if (curso.getGrado() == null || curso.getGrado().trim().isEmpty()) {
            throw new RuntimeException("El grado es obligatorio");
        }

        if (curso.getSeccion() == null || curso.getSeccion().trim().isEmpty()) {
            throw new RuntimeException("La sección es obligatoria");
        }

        curso.setNombre(curso.getNombre().trim());
        curso.setGrado(curso.getGrado().trim());
        curso.setSeccion(curso.getSeccion().trim());
    }
}