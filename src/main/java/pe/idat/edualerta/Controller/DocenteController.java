package pe.idat.edualerta.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import pe.idat.edualerta.Entity.Docente;
import pe.idat.edualerta.Service.DocenteService;

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

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}