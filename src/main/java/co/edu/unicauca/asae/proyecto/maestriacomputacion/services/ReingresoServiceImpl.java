package co.edu.unicauca.asae.proyecto.maestriacomputacion.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Estudiante;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Reingreso;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories.ReingresoRepository;

@Service
public class ReingresoServiceImpl implements IReingresoService {

	@Autowired
	private ReingresoRepository reingresoRepository;

	@Override
	public List<Reingreso> findAll() {
		return (List<Reingreso>) reingresoRepository.findAll();
	}

	@Override
	public Reingreso save(Reingreso objReingreso) {
		return reingresoRepository.save(objReingreso);
	}

	@Override
	public Reingreso findById(Integer rein_id) {
		return reingresoRepository.findById(rein_id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Integer rein_id) {
		reingresoRepository.deleteById(rein_id);
	}

	@Override
	@Transactional
	public Page<Reingreso> findAll(Pageable pageable) {
		return reingresoRepository.findAll(pageable);
	}

	@Override
	public Page<Reingreso> findByObjEstudiante(String id, Pageable pageable) {
		return reingresoRepository.getReingresosByEstudiante(id, pageable);
	}
}
