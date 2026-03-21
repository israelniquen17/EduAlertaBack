package pe.idat.edualerta.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResumenDTO {
    private int totalAlumnos;
    private int presentes;
    private int ausentes;
    private int tardanzas;
    private List<AsistenciaDashboardDTO> asistenciasHoy;
    private List<String> alertas;
}