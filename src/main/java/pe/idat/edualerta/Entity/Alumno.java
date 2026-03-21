package pe.idat.edualerta.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "alumno")
@Data
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_qr", unique = true, nullable = false)
    private String codigoQr;

    @Column(unique = true, nullable = false)
    private String dni;

    private String nombres;
    private String apellidos;

    private String grado;
    private String seccion;

    // RELACION CON TABLA NIVEL
    @ManyToOne
    @JoinColumn(name = "nivel_id")
    private Nivel nivel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.ACTIVO;

    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Padre> padres;

    public enum Estado {
        ACTIVO, INACTIVO
    }
}