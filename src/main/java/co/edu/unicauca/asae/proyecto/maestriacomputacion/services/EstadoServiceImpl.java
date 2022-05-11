package co.edu.unicauca.asae.proyecto.maestriacomputacion.services;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Estados;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories.IEstadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoServiceImpl implements IEstadoService{

    @Autowired
    private IEstadosRepository estadosRepository;

    @Override
    public Page<Estados> obtenerEstadosPorEstudiante(Long idEstudiante, Pageable pageable) {
        return estadosRepository.consultarEstadosPorEstudiante(idEstudiante, pageable);
    }

    @Override
    public List<Estados> obtenerEstadosPorEstudianteSinPaginacion(Long idEstudiante) {
        return estadosRepository.consultarEstadosPorEstudianteSinPaginacion(idEstudiante);
    }


}
