package pe.idat.edualerta.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pe.idat.edualerta.Entity.Nivel;
import pe.idat.edualerta.Repository.NivelRepository;

import java.util.List;

@RestController
@RequestMapping("/api/niveles")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class NivelController {

    private final NivelRepository repository;

    @GetMapping
    public List<Nivel> listar() {
        return repository.findAll();
    }
}