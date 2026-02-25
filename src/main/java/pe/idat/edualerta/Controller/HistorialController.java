package pe.idat.edualerta.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pe.idat.edualerta.Entity.Alumno;
import pe.idat.edualerta.Entity.HistorialQr;
import pe.idat.edualerta.Entity.Notificacion;
import pe.idat.edualerta.Entity.Padre;
import pe.idat.edualerta.Service.AlumnoService;
import pe.idat.edualerta.Service.HistorialService;
import pe.idat.edualerta.Service.PadreService;
import pe.idat.edualerta.Service.NotificacionService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/historial")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class HistorialController {

    private final HistorialService historialService;
    private final AlumnoService alumnoService;
    private final PadreService padreService;
    private final NotificacionService notificacionService;

    @PostMapping("/asistencia/dni/{dni}")
    public String alertarAsistenciaPorDni(@PathVariable String dni) {
        Alumno alumno = alumnoService.buscarPorDni(dni);
        if (alumno == null) return "Alumno no encontrado";

        HistorialQr registro = new HistorialQr();
        registro.setAlumno(alumno);
        registro.setFechaEscaneo(LocalDateTime.now());
        historialService.guardar(registro);

        Padre padre = padreService.buscarPorAlumno(alumno.getId());
        if (padre != null) {
            Notificacion notificacion = new Notificacion();
            notificacion.setPadre(padre);
            notificacion.setMensaje(
                "Su hijo " + alumno.getNombres() + " " + alumno.getApellidos() +
                " ingresó al colegio con éxito el " + registro.getFechaEscaneo()
            );
            notificacionService.guardar(notificacion);
            return "Notificación enviada a " + padre.getNombres() + " " + padre.getApellidos();
        }

        return "Asistencia registrada, pero no se encontró padre vinculado";
    }
}