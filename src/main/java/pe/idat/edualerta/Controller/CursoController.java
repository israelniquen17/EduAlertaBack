package pe.idat.edualerta.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pe.idat.edualerta.Entity.Curso;
import pe.idat.edualerta.Entity.Docente;
import pe.idat.edualerta.Repository.CursoRepository;
import pe.idat.edualerta.Repository.DocenteRepository;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class CursoController {

    private final CursoRepository cursoRepository;

    // 🔥 ESTA LÍNEA TE FALTABA
    private final DocenteRepository docenteRepository;

    @GetMapping
    public List<Curso> listar() {
        return cursoRepository.findAll();
    }

    @PostMapping
    public Curso guardar(@RequestBody Curso curso) {
        return cursoRepository.save(curso);
    }

    @PutMapping("/{id}")
    public Curso actualizar(@PathVariable Long id, @RequestBody Curso curso) {
        Curso existente = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        existente.setNombre(curso.getNombre());
        existente.setGrado(curso.getGrado());
        existente.setSeccion(curso.getSeccion());
        existente.setEstado(curso.getEstado());

        return cursoRepository.save(existente);
    }

    // 🔥 ENDPOINT PARA ASIGNAR DOCENTE
    @PutMapping("/{cursoId}/asignar-docente/{docenteId}")
    public Curso asignarDocente(@PathVariable Long cursoId,
                                @PathVariable Long docenteId) {

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Docente docente = docenteRepository.findById(docenteId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        curso.setDocente(docente);

        return cursoRepository.save(curso);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        cursoRepository.deleteById(id);
    }
}