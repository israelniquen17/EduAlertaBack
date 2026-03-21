package pe.idat.edualerta.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {

    private static final long TIMEOUT = 30L * 60L * 1000L;

    private final Map<Long, CopyOnWriteArrayList<SseEmitter>> padreEmitters = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<SseEmitter> dashboardEmitters = new CopyOnWriteArrayList<>();

    public SseEmitter suscribirPadre(Long usuarioId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);

        padreEmitters
                .computeIfAbsent(usuarioId, key -> new CopyOnWriteArrayList<>())
                .add(emitter);

        emitter.onCompletion(() -> removerPadreEmitter(usuarioId, emitter));
        emitter.onTimeout(() -> removerPadreEmitter(usuarioId, emitter));
        emitter.onError(error -> removerPadreEmitter(usuarioId, emitter));

        try {
            emitter.send(SseEmitter.event()
                    .name("INIT")
                    .data("Conexión SSE establecida"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    public SseEmitter suscribirDashboard() {
        SseEmitter emitter = new SseEmitter(TIMEOUT);

        dashboardEmitters.add(emitter);

        emitter.onCompletion(() -> dashboardEmitters.remove(emitter));
        emitter.onTimeout(() -> dashboardEmitters.remove(emitter));
        emitter.onError(error -> dashboardEmitters.remove(emitter));

        try {
            emitter.send(SseEmitter.event()
                    .name("INIT")
                    .data("Conexión SSE dashboard establecida"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    public void notificarPadre(Long usuarioId) {
        CopyOnWriteArrayList<SseEmitter> emitters = padreEmitters.get(usuarioId);
        if (emitters == null || emitters.isEmpty()) return;

        enviarEvento(emitters, "notificacion-update", "refresh");
    }

    public void actualizarDashboard() {
        if (dashboardEmitters.isEmpty()) return;

        enviarEvento(dashboardEmitters, "dashboard-update", "refresh");
    }

    private void enviarEvento(CopyOnWriteArrayList<SseEmitter> emitters, String eventName, String data) {
        ArrayList<SseEmitter> expirados = new ArrayList<>();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(data));
            } catch (IOException | IllegalStateException e) {
                emitter.complete();
                expirados.add(emitter);
            }
        }

        emitters.removeAll(expirados);
    }

    private void removerPadreEmitter(Long usuarioId, SseEmitter emitter) {
        CopyOnWriteArrayList<SseEmitter> emitters = padreEmitters.get(usuarioId);
        if (emitters == null) return;

        emitters.remove(emitter);

        if (emitters.isEmpty()) {
            padreEmitters.remove(usuarioId);
        }
    }
}