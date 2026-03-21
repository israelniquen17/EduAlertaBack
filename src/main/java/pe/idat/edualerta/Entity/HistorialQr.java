package pe.idat.edualerta.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_qr")
@Data
public class HistorialQr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "alumno_id")
    private Alumno alumno;

    @Column(name = "fecha_escaneo")
    private LocalDateTime fechaEscaneo;

    private String dispositivo;
}