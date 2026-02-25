package pe.idat.edualerta.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
@Getter
@Setter
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "padre_id")
    private Padre padre;

    private String mensaje;
    private Boolean leido = false;
    private LocalDateTime fecha = LocalDateTime.now();
}