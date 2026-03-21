package pe.idat.edualerta.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.idat.edualerta.DTO.AsistenciaDashboardDTO;
import pe.idat.edualerta.DTO.DashboardResumenDTO;
import pe.idat.edualerta.Entity.Alumno;
import pe.idat.edualerta.Repository.AlumnoRepository;
import pe.idat.edualerta.Repository.HistorialQrRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AlumnoRepository alumnoRepository;
    private final HistorialQrRepository historialQrRepository;

    public DashboardResumenDTO obtenerResumen() {
        List<Alumno> alumnos = alumnoRepository.findAll();
        List<Object[]> escaneosHoy = historialQrRepository.findUltimosEscaneosDeHoy();

        Map<Long, String> ingresoPorAlumno = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Object[] fila : escaneosHoy) {
            Long alumnoId = ((Number) fila[0]).longValue();

            String hora = "--:--";
            if (fila[1] instanceof java.sql.Timestamp ts) {
                hora = ts.toLocalDateTime().format(formatter);
            } else if (fila[1] instanceof LocalDateTime ldt) {
                hora = ldt.format(formatter);
            }

            ingresoPorAlumno.put(alumnoId, hora);
        }

        List<AsistenciaDashboardDTO> asistencias = new ArrayList<>();

        for (Alumno alumno : alumnos) {
            boolean presente = ingresoPorAlumno.containsKey(alumno.getId());

            asistencias.add(new AsistenciaDashboardDTO(
                    alumno.getDni(),
                    alumno.getNombres() + " " + alumno.getApellidos(),
                    alumno.getGrado(),
                    alumno.getSeccion(),
                    presente ? "Presente" : "Ausente",
                    presente ? ingresoPorAlumno.get(alumno.getId()) : "--:--"
            ));
        }

        int totalAlumnos = alumnos.size();
        int presentes = ingresoPorAlumno.size();
        int ausentes = totalAlumnos - presentes;
        int tardanzas = 0;

        List<String> alertas = new ArrayList<>();

        if (ausentes > 0) {
            alertas.add("Hay " + ausentes + " alumnos sin registro de ingreso hoy.");
        }

        if (presentes == 0) {
            alertas.add("Aún no se registran ingresos el día de hoy.");
        }

        if (alertas.isEmpty()) {
            alertas.add("Todo marcha correctamente. No hay alertas críticas.");
        }

        return new DashboardResumenDTO(
                totalAlumnos,
                presentes,
                ausentes,
                tardanzas,
                asistencias,
                alertas
        );
    }
}