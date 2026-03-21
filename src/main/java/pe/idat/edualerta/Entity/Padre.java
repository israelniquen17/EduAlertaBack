package pe.idat.edualerta.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "padre")
@Data
public class Padre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String dni;

    private String nombres;
    private String apellidos;
    private String telefono;

    // 🔥 CORTA LA RECURSIVIDAD
    @ManyToOne
    @JoinColumn(name = "alumno_id")
    @JsonBackReference
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.ACTIVO;

    public enum Estado {
        ACTIVO, INACTIVO
    }
}