package pe.idat.edualerta.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.idat.edualerta.DTO.AsistenciaRequest;
import pe.idat.edualerta.DTO.AsistenciaResponse;
import pe.idat.edualerta.Service.AsistenciaService;

@RestController
@RequestMapping("/api/asistencia")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    @PostMapping("/registrar")
    public ResponseEntity<AsistenciaResponse> registrar(@RequestBody AsistenciaRequest request) {
        AsistenciaResponse response = asistenciaService.registrarAsistencia(
                request.getQrData(),
                request.getDispositivo()
        );
        return ResponseEntity.ok(response);
    }
}