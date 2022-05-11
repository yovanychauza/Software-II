package co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Estudiante;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Prorroga;

public interface ProrrogaRepository extends JpaRepository<Prorroga, Long> {
    Page<Prorroga> findAll(Pageable pageable);
    Page<Prorroga> findProrrogaByObjEstudiante(Estudiante ObjEstudiante, Pageable pageable);
}
