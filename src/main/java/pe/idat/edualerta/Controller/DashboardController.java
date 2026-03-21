package pe.idat.edualerta.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pe.idat.edualerta.DTO.DashboardResumenDTO;
import pe.idat.edualerta.Service.DashboardService;
import pe.idat.edualerta.Service.SseService;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final SseService sseService;

    @GetMapping
    public DashboardResumenDTO obtenerResumen() {
        return dashboardService.obtenerResumen();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamDashboard() {
        return sseService.suscribirDashboard();
    }
}