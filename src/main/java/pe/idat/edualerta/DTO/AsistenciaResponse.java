package pe.idat.edualerta.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AsistenciaResponse {
    private String mensaje;
    private String alumno;
    private String hora;
}