package pe.idat.edualerta.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.idat.edualerta.Entity.Curso;
import pe.idat.edualerta.Entity.Docente;
import pe.idat.edualerta.Entity.Nivel;
import pe.idat.edualerta.Entity.Usuario;
import pe.idat.edualerta.Repository.CursoRepository;
import pe.idat.edualerta.Repository.DocenteRepository;
import pe.idat.edualerta.Repository.NivelRepository;
import pe.idat.edualerta.Repository.UsuarioRepository;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocenteService {

    private final DocenteRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final NivelRepository nivelRepository;
    private final CursoRepository cursoRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Docente> listar() {
        return repository.findAll();
    }

    public Docente guardar(Docente docente) {
        validarDocente(docente);

        if (repository.findByDni(docente.getDni()).isPresent()) {
            throw new RuntimeException("El DNI ya está registrado");
        }

        Nivel nivel = nivelRepository.findById(docente.getNivel().getId())
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));

        String usuarioGenerado = generarUsuario(docente.getNombres(), docente.getApellidos());

        if (usuarioRepository.findByUsuario(usuarioGenerado).isPresent()) {
            throw new RuntimeException("El usuario '" + usuarioGenerado + "' ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setUsuario(usuarioGenerado);
        usuario.setPassword(passwordEncoder.encode(docente.getDni().trim()));
        usuario.setRol(Usuario.Rol.DOCENTE);
        usuario.setEstado(Usuario.Estado.ACTIVO);

        usuarioRepository.save(usuario);

        docente.setUsuario(usuario);
        docente.setNivel(nivel);

        if (docente.getEstado() == null) {
            docente.setEstado(Docente.Estado.ACTIVO);
        }

        return repository.save(docente);
    }

    public Docente actualizar(Long id, Docente docente) {
        validarDocente(docente);

        Docente existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        if (!existente.getDni().equals(docente.getDni()) && repository.findByDni(docente.getDni()).isPresent()) {
            throw new RuntimeException("El DNI ya está registrado");
        }

        Nivel nivel = nivelRepository.findById(docente.getNivel().getId())
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));

        String nuevoUsuario = generarUsuario(docente.getNombres(), docente.getApellidos());

        Optional<Usuario> usuarioDuplicado = usuarioRepository.findByUsuario(nuevoUsuario);
        if (usuarioDuplicado.isPresent()
                && existente.getUsuario() != null
                && !usuarioDuplicado.get().getId().equals(existente.getUsuario().getId())) {
            throw new RuntimeException("El usuario '" + nuevoUsuario + "' ya existe");
        }

        existente.setDni(docente.getDni().trim());
        existente.setNombres(docente.getNombres().trim());
        existente.setApellidos(docente.getApellidos().trim());
        existente.setNivel(nivel);
        existente.setGrado(docente.getGrado().trim());
        existente.setSeccion(docente.getSeccion().trim());

        if (docente.getEstado() != null) {
            existente.setEstado(docente.getEstado());
        }

        if (existente.getUsuario() != null) {
            existente.getUsuario().setUsuario(nuevoUsuario);
            existente.getUsuario().setPassword(passwordEncoder.encode(docente.getDni().trim()));
            usuarioRepository.save(existente.getUsuario());
        }

        return repository.save(existente);
    }

    public Docente asignarCurso(Long docenteId, Long cursoId) {
        Docente docente = repository.findById(docenteId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        if (!docente.getNivel().getId().equals(curso.getNivel().getId())
                || !docente.getGrado().equalsIgnoreCase(curso.getGrado())
                || !docente.getSeccion().equalsIgnoreCase(curso.getSeccion())) {
            throw new RuntimeException("Solo puedes asignar cursos del mismo nivel, grado y sección del docente");
        }

        boolean repetido = docente.getCursos().stream()
                .anyMatch(c -> c.getId().equals(cursoId));

        if (!repetido) {
            docente.getCursos().add(curso);
        }

        return repository.save(docente);
    }

    public Docente quitarCurso(Long docenteId, Long cursoId) {
        Docente docente = repository.findById(docenteId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        docente.getCursos().removeIf(c -> c.getId().equals(cursoId));

        return repository.save(docente);
    }

    public void eliminar(Long id) {
        Docente docente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        docente.getCursos().clear();
        repository.save(docente);

        Usuario usuario = docente.getUsuario();
        repository.delete(docente);

        if (usuario != null) {
            usuarioRepository.deleteById(usuario.getId());
        }
    }

    private void validarDocente(Docente docente) {
        if (docente.getDni() == null || !docente.getDni().trim().matches("\\d{8}")) {
            throw new RuntimeException("El DNI debe tener 8 dígitos");
        }

        if (docente.getNombres() == null || docente.getNombres().trim().isEmpty()) {
            throw new RuntimeException("Los nombres son obligatorios");
        }

        if (docente.getApellidos() == null || docente.getApellidos().trim().isEmpty()) {
            throw new RuntimeException("Los apellidos son obligatorios");
        }

        if (docente.getNivel() == null || docente.getNivel().getId() == null) {
            throw new RuntimeException("El nivel es obligatorio");
        }

        if (docente.getGrado() == null || docente.getGrado().trim().isEmpty()) {
            throw new RuntimeException("El grado es obligatorio");
        }

        if (docente.getSeccion() == null || docente.getSeccion().trim().isEmpty()) {
            throw new RuntimeException("La sección es obligatoria");
        }

        docente.setDni(docente.getDni().trim());
        docente.setNombres(docente.getNombres().trim());
        docente.setApellidos(docente.getApellidos().trim());
        docente.setGrado(docente.getGrado().trim());
        docente.setSeccion(docente.getSeccion().trim());
    }

    private String generarUsuario(String nombres, String apellidos) {
        String inicial = nombres.trim().substring(0, 1).toLowerCase();
        String ap = normalizar(apellidos).replace(" ", "").toLowerCase();
        return inicial + ap;
    }

    private String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-zA-Z0-9 ]", "");
    }
}