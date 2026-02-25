package pe.idat.edualerta.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.idat.edualerta.Entity.Docente;
import pe.idat.edualerta.Repository.DocenteRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocenteService {

    private final DocenteRepository repository;

    public List<Docente> listar() {
        return repository.findAll();
    }

    public Docente guardar(Docente docente) {

        // 🔥 validar DNI duplicado
        repository.findByDni(docente.getDni())
                .ifPresent(d -> {
                    throw new RuntimeException("El DNI ya está registrado");
                });

        return repository.save(docente);
    }

    public Docente actualizar(Long id, Docente docente) {

        Docente existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        existente.setNombres(docente.getNombres());
        existente.setApellidos(docente.getApellidos());
        existente.setDni(docente.getDni());
        existente.setGrado(docente.getGrado());
        existente.setSeccion(docente.getSeccion());

        if (docente.getEstado() != null) {
            existente.setEstado(docente.getEstado());
        }

        return repository.save(existente);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}