package pe.idat.edualerta.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.idat.edualerta.Entity.Alumno;
import pe.idat.edualerta.Entity.Padre;
import pe.idat.edualerta.Entity.Usuario;
import pe.idat.edualerta.Repository.AlumnoRepository;
import pe.idat.edualerta.Repository.PadreRepository;
import pe.idat.edualerta.Repository.UsuarioRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    private final AlumnoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final PadreRepository padreRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    public List<Alumno> listar() {
        return repository.findAll();
    }

    public Alumno buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
    }

    public Alumno buscarPorDni(String dni) {
        return repository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Alumno con DNI " + dni + " no encontrado"));
    }

    private String generarCodigoQr(Alumno alumno) {
        try {
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("dni", alumno.getDni());
            payload.put("nombres", alumno.getNombres());
            payload.put("apellidos", alumno.getApellidos());
            payload.put("grado", alumno.getGrado());
            payload.put("seccion", alumno.getSeccion());
            payload.put("nivel", alumno.getNivel() != null ? alumno.getNivel().getNombre() : null);

            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al generar el código QR", e);
        }
    }

    private void validarAlumno(Alumno alumno) {
        if (alumno.getDni() == null || !alumno.getDni().matches("\\d{8}")) {
            throw new RuntimeException("El DNI del alumno debe tener 8 dígitos");
        }

        if (alumno.getNombres() == null || alumno.getNombres().trim().isEmpty()) {
            throw new RuntimeException("Los nombres del alumno son obligatorios");
        }

        if (alumno.getApellidos() == null || alumno.getApellidos().trim().isEmpty()) {
            throw new RuntimeException("Los apellidos del alumno son obligatorios");
        }

        if (alumno.getNivel() == null || alumno.getNivel().getId() == null) {
            throw new RuntimeException("El nivel es obligatorio");
        }

        if (alumno.getGrado() == null || alumno.getGrado().trim().isEmpty()) {
            throw new RuntimeException("El grado es obligatorio");
        }

        if (alumno.getSeccion() == null || alumno.getSeccion().trim().isEmpty()) {
            throw new RuntimeException("La sección es obligatoria");
        }
    }

    private void validarPadre(Padre padre) {
        if (padre.getDni() == null || !padre.getDni().matches("\\d{8}")) {
            throw new RuntimeException("El DNI del padre/madre debe tener 8 dígitos");
        }

        if (padre.getNombres() == null || padre.getNombres().trim().isEmpty()) {
            throw new RuntimeException("Los nombres del padre/madre son obligatorios");
        }

        if (padre.getApellidos() == null || padre.getApellidos().trim().isEmpty()) {
            throw new RuntimeException("Los apellidos del padre/madre son obligatorios");
        }

        if (padre.getTelefono() == null || !padre.getTelefono().matches("\\d{9}")) {
            throw new RuntimeException("El teléfono del padre/madre debe tener 9 dígitos");
        }
    }

    public Alumno guardar(Alumno alumno) {
        validarAlumno(alumno);

        if (repository.existsByDni(alumno.getDni())) {
            throw new RuntimeException("El DNI del alumno ya existe");
        }

        alumno.setCodigoQr(generarCodigoQr(alumno));

        if (alumno.getPadres() != null) {
            for (Padre padre : alumno.getPadres()) {
                validarPadre(padre);

                if (padreRepository.existsByDni(padre.getDni())) {
                    throw new RuntimeException("El DNI del padre/madre ya existe");
                }

                Usuario usuario = new Usuario();
                usuario.setUsuario(padre.getDni());
                String nombres = padre.getNombres().trim();
                String passwordInicial = nombres.substring(0, 1).toLowerCase() + padre.getDni();
                    usuario.setPassword(passwordEncoder.encode(passwordInicial));
                usuario.setRol(Usuario.Rol.PADRE);
                usuario.setEstado(Usuario.Estado.ACTIVO);

                usuarioRepository.save(usuario);

                padre.setUsuario(usuario);
                padre.setAlumno(alumno);
            }
        }

        return repository.save(alumno);
    }

    public Alumno actualizar(Long id, Alumno alumno) {
        Alumno existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        if (!existente.getDni().equals(alumno.getDni()) && repository.existsByDni(alumno.getDni())) {
            throw new RuntimeException("El DNI del alumno ya existe");
        }

        existente.setNombres(alumno.getNombres());
        existente.setApellidos(alumno.getApellidos());
        existente.setGrado(alumno.getGrado());
        existente.setNivel(alumno.getNivel());
        existente.setSeccion(alumno.getSeccion());
        existente.setDni(alumno.getDni());

        if (alumno.getEstado() != null) {
            existente.setEstado(alumno.getEstado());
        }

        existente.setCodigoQr(generarCodigoQr(existente));

        if (alumno.getPadres() != null && !alumno.getPadres().isEmpty()) {
            Padre padreNuevo = alumno.getPadres().get(0);

            if (padreNuevo.getDni() == null || !padreNuevo.getDni().matches("\\d{8}")) {
                throw new RuntimeException("El DNI del padre/madre debe tener 8 dígitos");
            }

            if (padreNuevo.getTelefono() == null || !padreNuevo.getTelefono().matches("\\d{9}")) {
                throw new RuntimeException("El teléfono del padre/madre debe tener 9 dígitos");
            }

            if (existente.getPadres() != null && !existente.getPadres().isEmpty()) {
                Padre padreExistente = existente.getPadres().get(0);

                padreExistente.setDni(padreNuevo.getDni());
                padreExistente.setNombres(padreNuevo.getNombres());
                padreExistente.setApellidos(padreNuevo.getApellidos());
                padreExistente.setTelefono(padreNuevo.getTelefono());

                if (padreExistente.getUsuario() != null) {
                    padreExistente.getUsuario().setUsuario(padreNuevo.getDni());
                    padreExistente.getUsuario().setPassword(passwordEncoder.encode(padreNuevo.getDni()));
                    usuarioRepository.save(padreExistente.getUsuario());
                }

            } else {
                Usuario usuario = new Usuario();
                usuario.setUsuario(padreNuevo.getDni());
                usuario.setPassword(passwordEncoder.encode(padreNuevo.getDni()));
                usuario.setRol(Usuario.Rol.PADRE);
                usuario.setEstado(Usuario.Estado.ACTIVO);

                usuarioRepository.save(usuario);

                padreNuevo.setUsuario(usuario);
                padreNuevo.setAlumno(existente);

                existente.getPadres().add(padreNuevo);
            }
        }

        return repository.save(existente);
    }

    public void eliminar(Long id) {
        Alumno alumno = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        repository.delete(alumno);
    }
}