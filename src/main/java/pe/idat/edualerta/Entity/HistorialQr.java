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

    @ManyToOne
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @Column(name = "fecha_escaneo", nullable = false)
    private LocalDateTime fechaEscaneo;

    private String dispositivo; // Opcional: desde qué dispositivo se escaneó
}