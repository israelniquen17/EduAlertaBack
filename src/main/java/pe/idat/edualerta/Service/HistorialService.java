package pe.idat.edualerta.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.idat.edualerta.Entity.HistorialQr;
import pe.idat.edualerta.Repository.HistorialRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistorialService {

    private final HistorialRepository repository;

    public HistorialQr guardar(HistorialQr historial) {
        return repository.save(historial);
    }

    public List<HistorialQr> listar() {
        return repository.findAll();
    }

    public HistorialQr buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}