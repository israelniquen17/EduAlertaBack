package pe.idat.edualerta.Controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pe.idat.edualerta.Entity.Alumno;
import pe.idat.edualerta.Entity.Notificacion;
import pe.idat.edualerta.Entity.Padre;
import pe.idat.edualerta.Service.PadreService;
import pe.idat.edualerta.Service.SseService;

@RestController
@RequestMapping("/api/padres")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class PadreController {

    private final PadreService service;
    private final SseService sseService;

    @GetMapping
    public List<Padre> listar() {
        return service.listar();
    }

    @PostMapping
    public Padre guardar(@RequestBody Padre padre) {
        return service.guardar(padre);
    }

    @PutMapping("/{id}")
    public Padre actualizar(@PathVariable Long id, @RequestBody Padre padre) {
        return service.actualizar(id, padre);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Padre> obtenerPadrePorUsuario(@PathVariable Long usuarioId) {
        Padre padre = service.obtenerPadrePorUsuario(usuarioId);
        return ResponseEntity.ok(padre);
    }

    @GetMapping("/usuario/{usuarioId}/alumno")
    public ResponseEntity<Alumno> getAlumnoPorPadre(@PathVariable Long usuarioId) {
        Alumno alumno = service.obtenerAlumnoPorPadre(usuarioId);
        return ResponseEntity.ok(alumno);
    }

    @GetMapping("/alumno/{alumnoId}")
    public ResponseEntity<Padre> getPadrePorAlumno(@PathVariable Long alumnoId) {
        Padre padre = service.buscarPorAlumno(alumnoId);
        return ResponseEntity.ok(padre);
    }

    @GetMapping("/usuario/{usuarioId}/notificaciones")
    public ResponseEntity<List<Notificacion>> obtenerNotificaciones(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.obtenerNotificacionesPorUsuario(usuarioId));
    }

    @PutMapping("/usuario/{usuarioId}/notificaciones/marcar-leidas")
    public ResponseEntity<String> marcarLeidas(@PathVariable Long usuarioId) {
        service.marcarNotificacionesLeidasPorUsuario(usuarioId);
        return ResponseEntity.ok("Notificaciones marcadas como leídas");
    }

    @GetMapping(value = "/usuario/{usuarioId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamPadre(@PathVariable Long usuarioId) {
        return sseService.suscribirPadre(usuarioId);
    }
}