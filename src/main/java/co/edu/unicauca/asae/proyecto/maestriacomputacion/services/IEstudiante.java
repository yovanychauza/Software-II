package co.edu.unicauca.asae.proyecto.maestriacomputacion.services;

import java.util.List;
import java.util.Optional;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Estudiante;

public interface IEstudiante {
	
	public Estudiante save(Estudiante objEstudiante);
	public List<Estudiante> listStudents();
	public Optional<Estudiante> dataStudent(Long id);
	public void delete(Estudiante estudiante);
	
}
