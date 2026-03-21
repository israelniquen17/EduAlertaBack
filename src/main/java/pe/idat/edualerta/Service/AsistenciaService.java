package pe.idat.edualerta.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.idat.edualerta.DTO.AsistenciaResponse;
import pe.idat.edualerta.Entity.Alumno;
import pe.idat.edualerta.Entity.HistorialQr;
import pe.idat.edualerta.Entity.Notificacion;
import pe.idat.edualerta.Entity.Padre;
import pe.idat.edualerta.Repository.AlumnoRepository;
import pe.idat.edualerta.Repository.HistorialQrRepository;
import pe.idat.edualerta.Repository.NotificacionRepository;
import pe.idat.edualerta.Repository.PadreRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsistenciaService {

    private final AlumnoRepository alumnoRepository;
    private final HistorialQrRepository historialQrRepository;
    private final PadreRepository padreRepository;
    private final NotificacionRepository notificacionRepository;
    private final ObjectMapper objectMapper;
    private final SseService sseService;

    public AsistenciaResponse registrarAsistencia(String qrData, String dispositivo) {
        try {
            String dni = extraerDni(qrData);

            Alumno alumno = alumnoRepository.findByDni(dni)
                    .orElseThrow(() -> new RuntimeException("Alumno no encontrado con DNI " + dni));

            LocalDateTime ahora = LocalDateTime.now();

            HistorialQr historial = new HistorialQr();
            historial.setAlumno(alumno);
            historial.setFechaEscaneo(ahora);
            historial.setDispositivo(
                    dispositivo != null && !dispositivo.isBlank()
                            ? dispositivo
                            : "Scanner Web"
            );

            historialQrRepository.save(historial);

            List<Padre> padres = padreRepository.findAllByAlumnoId(alumno.getId());

            for (Padre padre : padres) {
                Notificacion notif = new Notificacion();
                notif.setPadre(padre);
                notif.setMensaje("Se registró la asistencia de " +
                        alumno.getNombres() + " " + alumno.getApellidos());
                notif.setLeido(false);
                notif.setFecha(ahora);

                notificacionRepository.save(notif);

                if (padre.getUsuario() != null && padre.getUsuario().getId() != null) {
                    sseService.notificarPadre(padre.getUsuario().getId());
                }
            }

            sseService.actualizarDashboard();

            return new AsistenciaResponse(
                    "Asistencia registrada correctamente",
                    alumno.getNombres() + " " + alumno.getApellidos(),
                    ahora.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            );

        } catch (Exception e) {
            throw new RuntimeException("Error al registrar asistencia: " + e.getMessage(), e);
        }
    }

    private String extraerDni(String qrData) {
        if (qrData == null || qrData.isBlank()) {
            throw new RuntimeException("El QR está vacío");
        }

        String valor = qrData.trim();

        try {
            JsonNode json = objectMapper.readTree(valor);
            JsonNode dniNode = json.get("dni");

            if (dniNode != null && !dniNode.asText().isBlank()) {
                return dniNode.asText().trim();
            }
        } catch (Exception ignored) {
        }

        if (valor.startsWith("QR-")) {
            valor = valor.substring(3).trim();
        }

        if (!valor.matches("\\d{8}")) {
            throw new RuntimeException("No se pudo obtener un DNI válido desde el QR");
        }

        return valor;
    }
}