package pe.idat.edualerta.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.idat.edualerta.Entity.HistorialQr;

import java.util.List;

@Repository
public interface HistorialQrRepository extends JpaRepository<HistorialQr, Long> {

    @Query(value = """
        SELECT alumno_id, MAX(fecha_escaneo) as ultima_fecha
        FROM historial_qr
        WHERE DATE(fecha_escaneo) = CURDATE()
        GROUP BY alumno_id
        """, nativeQuery = true)
    List<Object[]> findUltimosEscaneosDeHoy();
}