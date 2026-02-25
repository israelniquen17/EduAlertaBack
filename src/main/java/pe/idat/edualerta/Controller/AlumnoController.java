package pe.idat.edualerta.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.idat.edualerta.Entity.Alumno;
import pe.idat.edualerta.Service.AlumnoService;
import pe.idat.edualerta.Service.PadreService;

import java.util.List;

@RestController
@RequestMapping("/api/alumnos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AlumnoController {

    private final AlumnoService service;
    private final PadreService padreService;

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

    @GetMapping("/dni/{dni}")
    public Alumno buscarPorDni(@PathVariable String dni) {
        return service.buscarPorDni(dni);
    }

    @GetMapping("/padres/{usuarioId}/alumnos")
    public ResponseEntity<Alumno> getAlumnoPorPadre(@PathVariable Long usuarioId) {
        Alumno alumno = padreService.obtenerAlumnoPorPadre(usuarioId);
        return ResponseEntity.ok(alumno);
    }
}