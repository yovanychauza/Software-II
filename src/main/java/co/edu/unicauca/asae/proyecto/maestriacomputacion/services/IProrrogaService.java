package co.edu.unicauca.asae.proyecto.maestriacomputacion.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Prorroga;

public interface IProrrogaService {
    // Permite guardar un registro de prorroga
    public Prorroga save(Prorroga prorroga);

    // Buscar Prorroga por identificador
    public Optional<Prorroga> findById(Long identificador);

    // Listar todas las Prorroga
    public Page<Prorroga> findAll(Pageable pageable);

    // Eliminar prorroga
    public Prorroga deleteProrroga(Long identificador);

    /**
     * Permiter realizar la paginación al listar las prorrogas de un estudiante
     * @param estudianteId identificación del estudiante
     * @param pageable
     * @return
     */
    public Page<Prorroga> prorrogasEstudiante(Long estudianteId, Pageable pageable);
}
