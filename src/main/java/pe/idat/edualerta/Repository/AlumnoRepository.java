package pe.idat.edualerta.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.idat.edualerta.Entity.Alumno;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
}
