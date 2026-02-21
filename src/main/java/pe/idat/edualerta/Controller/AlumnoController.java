package pe.idat.edualerta.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pe.idat.edualerta.Entity.Alumno;
import pe.idat.edualerta.Service.AlumnoService;

import java.util.List;

@RestController
@RequestMapping("/api/alumnos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AlumnoController {

    private final AlumnoService service;

    @GetMapping
    public List<Alumno> listar() {
        return service.listar();
    }

    @PostMapping
    public Alumno guardar(@RequestBody Alumno alumno) {
        return service.guardar(alumno);
    }

    @PutMapping("/{id}")
    public Alumno actualizar(@PathVariable Long id,
                              @RequestBody Alumno alumno) {
        return service.actualizar(id, alumno);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}