package co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Estudiante;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Reingreso;

public interface ReingresoRepository extends JpaRepository<Reingreso, Integer> {

    @Query(value = "SELECT * " +
            "FROM reingresos r " +
            "WHERE r.rein_est_codigo = :estCodigo ", nativeQuery = true)
    public Page<Reingreso> getReingresosByEstudiante(@Param("estCodigo") String estCodigo,
            Pageable pageable);
}
