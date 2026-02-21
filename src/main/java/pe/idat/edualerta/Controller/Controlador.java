package pe.idat.edualerta.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controlador {

    @GetMapping("/test")
    public String test() {
        return "Conectado correctamente";
    }

}
