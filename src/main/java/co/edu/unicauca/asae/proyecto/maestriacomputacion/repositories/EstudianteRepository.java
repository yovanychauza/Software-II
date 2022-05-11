package co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Estudiante;
import org.springframework.data.repository.query.Param;

public interface EstudianteRepository extends CrudRepository<Estudiante, Long>{

}
