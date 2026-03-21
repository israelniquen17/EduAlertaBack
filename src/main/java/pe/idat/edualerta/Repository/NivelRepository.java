package pe.idat.edualerta.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.idat.edualerta.Entity.Nivel;

@Repository
public interface NivelRepository extends JpaRepository<Nivel, Long> {
}