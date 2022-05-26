package co.edu.unicauca.asae.proyecto.maestriacomputacion.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Estudiante;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.helpers.ExcelHelper;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories.EstudianteRepository;

@Service
public class ExcelService {
	
	@Autowired
	private EstudianteRepository repository;

	public void save(MultipartFile file) {
		try {
			List<Estudiante> tutorials = ExcelHelper.saveEstudiantes(file.getInputStream());
			repository.saveAll(tutorials);
		} catch (IOException e) {
			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}

}
