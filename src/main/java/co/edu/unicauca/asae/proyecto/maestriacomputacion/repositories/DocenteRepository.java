package co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Docente;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface DocenteRepository extends CrudRepository<Docente, Long> {
    public List<Docente> findByNombres(String nombres);

    public List<Docente> findByApellidos(String apellidos);
}
