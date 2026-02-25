package pe.idat.edualerta.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.idat.edualerta.Entity.Curso;
import pe.idat.edualerta.Entity.Docente;
import pe.idat.edualerta.Repository.CursoRepository;
import pe.idat.edualerta.Repository.DocenteRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository repository;
    private final DocenteRepository docenteRepository;

    public List<Curso> listar() {
        return repository.findAll();
    }

    public Curso guardar(Curso curso) {

    if (curso.getDocente() != null && curso.getDocente().getId() != null) {
        Docente docente = docenteRepository.findById(curso.getDocente().getId())
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        curso.setDocente(docente);
    }

    return repository.save(curso); // ✅ corregido
}

    public Curso actualizar(Long id, Curso curso) {
        Curso existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        existente.setNombre(curso.getNombre());
        existente.setGrado(curso.getGrado());
        existente.setSeccion(curso.getSeccion());

        if (curso.getEstado() != null) {
            existente.setEstado(curso.getEstado());
        }

        return repository.save(existente);
    }

    // 🔥 NUEVO MÉTODO PARA ASIGNAR DOCENTE
    public Curso asignarDocente(Long cursoId, Long docenteId) {

        Curso curso = repository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Docente docente = docenteRepository.findById(docenteId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        curso.setDocente(docente);

        return repository.save(curso);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}