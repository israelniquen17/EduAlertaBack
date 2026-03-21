package pe.idat.edualerta.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "curso")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nivel_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Nivel nivel;

    @Column(nullable = false)
    private String grado;

    @Column(nullable = false)
    private String seccion;

    @ManyToMany(mappedBy = "cursos", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Docente> docentes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.ACTIVO;

    public enum Estado {
        ACTIVO, INACTIVO
    }
}