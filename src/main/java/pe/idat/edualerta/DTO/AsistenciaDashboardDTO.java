package pe.idat.edualerta.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsistenciaDashboardDTO {
    private String dni;
    private String nombre;
    private String grado;
    private String seccion;
    private String estado;
    private String horaIngreso;
}