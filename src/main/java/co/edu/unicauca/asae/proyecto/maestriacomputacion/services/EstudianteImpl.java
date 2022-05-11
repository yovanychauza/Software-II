package co.edu.unicauca.asae.proyecto.maestriacomputacion.services;

import java.util.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Estudiante;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories.EstudianteRepository;

@Service
public class EstudianteImpl implements IEstudiante{
	
	@Autowired
	private EstudianteRepository objDao;
	
	@Override
	public Estudiante save(Estudiante objEstudiante) {
		return this.objDao.save(objEstudiante);
	}

	@Override
	public List<Estudiante> listStudents() {
		return (List<Estudiante>) objDao.findAll();
	}

	@Override
	public Optional<Estudiante> dataStudent(Long Codigo) {
		
		return objDao.findById(Codigo);
	}

	@Override
	public void delete(Estudiante estudiante) {
		objDao.delete(estudiante);
	}


}
