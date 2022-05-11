package co.edu.unicauca.asae.proyecto.maestriacomputacion.services;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Estados;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEstadoService {
    public Page<Estados> obtenerEstadosPorEstudiante(Long idEstudiante, Pageable pageable);
    public List<Estados> obtenerEstadosPorEstudianteSinPaginacion(Long idEstudiante);
}
