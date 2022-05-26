package co.edu.unicauca.asae.proyecto.maestriacomputacion.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Reingreso;

public interface IReingresoService {

    public List<Reingreso> findAll();

    public Reingreso save(Reingreso objReingreso);

    public Reingreso findById(Integer rein_id);

    public void delete(Integer id);

    public Page<Reingreso> findAll(Pageable pageable);

    public Page<Reingreso> findByObjEstudiante(String objEstudiante, Pageable pageable);

}
