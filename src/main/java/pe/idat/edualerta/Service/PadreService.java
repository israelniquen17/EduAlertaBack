package pe.idat.edualerta.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.idat.edualerta.Entity.Alumno;
import pe.idat.edualerta.Entity.Notificacion;
import pe.idat.edualerta.Entity.Padre;
import pe.idat.edualerta.Entity.Usuario;
import pe.idat.edualerta.Repository.NotificacionRepository;
import pe.idat.edualerta.Repository.PadreRepository;
import pe.idat.edualerta.Repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PadreService {

    private final PadreRepository repository;
    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private void validarPadre(Padre padre) {
        if (padre == null) {
            throw new RuntimeException("Los datos del padre son obligatorios");
        }

        if (padre.getDni() == null || !padre.getDni().trim().matches("\\d{8}")) {
            throw new RuntimeException("El DNI del padre debe tener 8 dígitos");
        }

        if (padre.getNombres() == null || padre.getNombres().trim().isEmpty()) {
            throw new RuntimeException("Los nombres del padre son obligatorios");
        }

        if (padre.getApellidos() == null || padre.getApellidos().trim().isEmpty()) {
            throw new RuntimeException("Los apellidos del padre son obligatorios");
        }

        if (padre.getTelefono() == null || !padre.getTelefono().trim().matches("\\d{9}")) {
            throw new RuntimeException("El teléfono del padre debe tener 9 dígitos");
        }
    }

    public List<Padre> listar() {
        return repository.findAll();
    }

    public Padre guardar(Padre padre) {
        validarPadre(padre);

        String dni = padre.getDni().trim();
        String nombres = padre.getNombres().trim();
        String apellidos = padre.getApellidos().trim();
        String telefono = padre.getTelefono().trim();

        if (repository.existsByDni(dni)) {
            throw new RuntimeException("El DNI del padre ya existe");
        }

        if (usuarioRepository.findByUsuario(dni).isPresent()) {
            throw new RuntimeException("Ya existe un usuario con ese DNI");
        }

        padre.setDni(dni);
        padre.setNombres(nombres);
        padre.setApellidos(apellidos);
        padre.setTelefono(telefono);

        Usuario usuario = new Usuario();
        usuario.setUsuario(dni);
        usuario.setPassword(passwordEncoder.encode(dni));
        usuario.setRol(Usuario.Rol.PADRE);
        usuario.setEstado(Usuario.Estado.ACTIVO);

        usuarioRepository.save(usuario);

        padre.setUsuario(usuario);

        if (padre.getEstado() == null) {
            padre.setEstado(Padre.Estado.ACTIVO);
        }

        return repository.save(padre);
    }

    public Padre actualizar(Long id, Padre padre) {
        validarPadre(padre);

        Padre existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Padre no encontrado"));

        String dniNuevo = padre.getDni().trim();
        String nombres = padre.getNombres().trim();
        String apellidos = padre.getApellidos().trim();
        String telefono = padre.getTelefono().trim();

        if (!existente.getDni().equals(dniNuevo) && repository.existsByDni(dniNuevo)) {
            throw new RuntimeException("El DNI del padre ya existe");
        }

        existente.setDni(dniNuevo);
        existente.setNombres(nombres);
        existente.setApellidos(apellidos);
        existente.setTelefono(telefono);

        if (padre.getAlumno() != null) {
            existente.setAlumno(padre.getAlumno());
        }

        if (padre.getEstado() != null) {
            existente.setEstado(padre.getEstado());
        }

        if (existente.getUsuario() != null) {
            Optional<Usuario> usuarioDuplicado = usuarioRepository.findByUsuario(dniNuevo);

            if (usuarioDuplicado.isPresent()
                    && !usuarioDuplicado.get().getId().equals(existente.getUsuario().getId())) {
                throw new RuntimeException("Ya existe otro usuario con ese DNI");
            }

            existente.getUsuario().setUsuario(dniNuevo);
            existente.getUsuario().setPassword(passwordEncoder.encode(dniNuevo));
            usuarioRepository.save(existente.getUsuario());
        }

        return repository.save(existente);
    }

    public void eliminar(Long id) {
        Padre padre = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Padre no encontrado"));

        Usuario usuario = padre.getUsuario();

        repository.delete(padre);

        if (usuario != null) {
            usuarioRepository.deleteById(usuario.getId());
        }
    }

    public Padre obtenerPadrePorUsuario(Long usuarioId) {
        return repository.findByUsuario_Id(usuarioId)
                .orElseThrow(() -> new RuntimeException("No se encontró un padre asociado al usuario " + usuarioId));
    }

    public Alumno obtenerAlumnoPorPadre(Long usuarioId) {
        Padre padre = repository.findByUsuario_Id(usuarioId)
                .orElseThrow(() -> new RuntimeException("Padre no encontrado"));

        if (padre.getAlumno() == null) {
            throw new RuntimeException("El padre no tiene alumno asociado");
        }

        return padre.getAlumno();
    }

    public Padre buscarPorAlumno(Long alumnoId) {
        Padre padre = repository.findByAlumnoId(alumnoId);

        if (padre == null) {
            throw new RuntimeException("No se encontró padre para el alumno");
        }

        return padre;
    }

    public List<Notificacion> obtenerNotificacionesPorPadre(Long padreId) {
        return notificacionRepository.findByPadreIdOrderByFechaDesc(padreId);
    }

    public List<Notificacion> obtenerNotificacionesPorUsuario(Long usuarioId) {
        Padre padre = repository.findByUsuario_Id(usuarioId)
                .orElseThrow(() -> new RuntimeException("No se encontró un padre asociado al usuario " + usuarioId));

        return notificacionRepository.findByPadreIdAndLeidoFalseOrderByFechaDesc(padre.getId());
    }

    public void marcarNotificacionesLeidasPorUsuario(Long usuarioId) {
        Padre padre = repository.findByUsuario_Id(usuarioId)
                .orElseThrow(() -> new RuntimeException("No se encontró un padre asociado al usuario " + usuarioId));

        List<Notificacion> notificaciones =
                notificacionRepository.findByPadreIdAndLeidoFalseOrderByFechaDesc(padre.getId());

        notificaciones.forEach(n -> n.setLeido(true));
        notificacionRepository.saveAll(notificaciones);
    }
}