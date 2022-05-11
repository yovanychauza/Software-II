package co.edu.unicauca.asae.proyecto.maestriacomputacion.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Estudiante;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Prorroga;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories.EstudianteRepository;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories.ProrrogaRepository;

@Service
public class ProrrogaServiceImpl implements IProrrogaService {
    @Autowired
    private ProrrogaRepository prorrogaRepository;

    @Autowired
	private EstudianteRepository estudianteRepository;
    
    @Override
    @Transactional
    public Prorroga save(Prorroga prorroga) {
        return this.prorrogaRepository.save(prorroga);
    }

    @Override
    public Optional<Prorroga> findById(Long identificador) {
        return this.prorrogaRepository.findById(identificador);
    }

    @Override
    public Prorroga deleteProrroga(Long identificador) {
        Optional<Prorroga> prorroga_buscada = null;
        prorroga_buscada = this.prorrogaRepository.findById(identificador);
        if (prorroga_buscada != null) {
            this.prorrogaRepository.delete(prorroga_buscada.get());
        }
        return prorroga_buscada.get();
    }

    @Override
    public Page<Prorroga> findAll(Pageable pageable) {
        /*List<Prorroga> lista_Prorroga = new LinkedList<Prorroga>();
        Iterable<Prorroga> Prorroga_bd = this.prorrogaRepository.findAll();
        for (Prorroga prorroga : Prorroga_bd) {
            lista_Prorroga.add(prorroga);
        }*/
        return this.prorrogaRepository.findAll(pageable);
    }

    @Override
    public Page<Prorroga> prorrogasEstudiante(Long estudianteId, Pageable pageable) {
        Optional<Estudiante> estudianteDB = this.estudianteRepository.findById(estudianteId);
        return this.prorrogaRepository.findProrrogaByObjEstudiante(estudianteDB.get(), pageable);
    }
}
