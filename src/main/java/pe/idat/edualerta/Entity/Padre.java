package pe.idat.edualerta.Entity;

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

    // Relación con Alumno
    @ManyToOne
    @JoinColumn(name = "alumno_id")
    private Alumno alumno;

    // Relación con Usuario
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.ACTIVO;

    public enum Estado {
        ACTIVO, INACTIVO
    }
}