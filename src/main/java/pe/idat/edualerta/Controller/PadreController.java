package pe.idat.edualerta.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import pe.idat.edualerta.Entity.Padre;
import pe.idat.edualerta.Entity.Alumno;
import pe.idat.edualerta.Entity.Notificacion;
import pe.idat.edualerta.Service.PadreService;

@RestController
@RequestMapping("/api/padres")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class PadreController {

    private final PadreService service;

    // 🔹 Listar todos los padres
    @GetMapping
    public List<Padre> listar() {
        return service.listar();
    }

    // 🔹 Guardar un padre
    @PostMapping
    public Padre guardar(@RequestBody Padre padre) {
        return service.guardar(padre);
    }

    // 🔹 Actualizar un padre existente
    @PutMapping("/{id}")
    public Padre actualizar(@PathVariable Long id, @RequestBody Padre padre) {
        return service.actualizar(id, padre);
    }

    // 🔹 Eliminar un padre por ID
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    // 🔹 Obtener el padre y su alumno por usuario
    @GetMapping("/usuario/{usuarioId}/alumno")
    public ResponseEntity<Alumno> getAlumnoPorPadre(@PathVariable Long usuarioId) {
        Alumno alumno = service.obtenerAlumnoPorPadre(usuarioId);
        return ResponseEntity.ok(alumno);
    }

    // 🔹 Buscar padre por alumno (para notificaciones)
    @GetMapping("/alumno/{alumnoId}")
    public ResponseEntity<Padre> getPadrePorAlumno(@PathVariable Long alumnoId) {
        Padre padre = service.buscarPorAlumno(alumnoId);
        return ResponseEntity.ok(padre);
    }

 // Obtener notificaciones de un padre
    @GetMapping("/{id}/notificaciones")
    public ResponseEntity<List<Notificacion>> obtenerNotificaciones(@PathVariable Long id) {
        List<Notificacion> notificaciones = service.obtenerNotificacionesPorPadre(id);
        return ResponseEntity.ok(notificaciones);
    }
}