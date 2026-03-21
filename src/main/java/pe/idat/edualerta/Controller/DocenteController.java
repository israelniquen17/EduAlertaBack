package pe.idat.edualerta.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pe.idat.edualerta.Entity.Docente;
import pe.idat.edualerta.Service.DocenteService;

import java.util.List;

@RestController
@RequestMapping("/api/docentes")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class DocenteController {

    private final DocenteService service;

    @GetMapping
    public List<Docente> listar() {
        return service.listar();
    }

    @PostMapping
    public Docente guardar(@RequestBody Docente docente) {
        return service.guardar(docente);
    }

    @PutMapping("/{id}")
    public Docente actualizar(@PathVariable Long id, @RequestBody Docente docente) {
        return service.actualizar(id, docente);
    }

    @PutMapping("/{docenteId}/asignar-curso/{cursoId}")
    public Docente asignarCurso(@PathVariable Long docenteId, @PathVariable Long cursoId) {
        return service.asignarCurso(docenteId, cursoId);
    }

    @DeleteMapping("/{id}/quitar-curso/{cursoId}")
    public Docente quitarCurso(@PathVariable Long id, @PathVariable Long cursoId) {
        return service.quitarCurso(id, cursoId);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}