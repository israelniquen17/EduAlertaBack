package pe.idat.edualerta.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
@Data
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "padre_id")
    private Padre padre;

    private String mensaje;

    private Boolean leido = false;

    private LocalDateTime fecha;
}