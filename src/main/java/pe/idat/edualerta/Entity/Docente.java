package pe.idat.edualerta.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "docente")
@Data
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String dni;

    private String nombres;
    private String apellidos;
    private String grado;
    private String seccion;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.ACTIVO;

    // 🔥 ESTA PARTE ES LA QUE TE FALTA
    @OneToMany(mappedBy = "docente", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("docente")
    private List<Curso> cursos;

    public enum Estado {
        ACTIVO, INACTIVO
    }
}