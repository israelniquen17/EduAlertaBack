package pe.idat.edualerta.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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

    @Enumerated(EnumType.STRING)
    private Estado estado;

    public enum Estado {
        ACTIVO, INACTIVO
    }
}
