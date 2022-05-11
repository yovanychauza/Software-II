package co.edu.unicauca.asae.proyecto.maestriacomputacion.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.validation.Valid;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Asignatura;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Estados;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.services.IEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Estudiante;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.helpers.ExcelHelper;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.services.ExcelService;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.services.IEstudiante;


@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/estudiante")
public class EstudianteController {

	@Autowired
	private IEstudiante objEstService;

	@Autowired
	private IEstadoService estadoService;

	@Autowired
	ExcelService fileService;

	@PostMapping("/save")
	public ResponseEntity<?> save(@Valid @RequestBody Estudiante objEstudiante) {
		Map<String, Object> response = new HashMap<>();
		Estudiante objDoc = new Estudiante();
		try {

			// Ajuste temporal mienstras se determina por que no asigna el codigo asi se
			Random aleatorio = new Random(System.currentTimeMillis());
			Integer intAletorio = aleatorio.nextInt(10000);
			objEstudiante.setCodigo(intAletorio.toString());
			objDoc = this.objEstService.save(objEstudiante);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la inserción en la base de datos");
			response.put("Error", e.getMessage() + " " + e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Estudiante>(objDoc, HttpStatus.OK);
	}

	@PostMapping("/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		Map<String, Object> response = new HashMap<>();

		if (ExcelHelper.hasExcelFormat(file)) {
			try {
				fileService.save(file);
			} catch (Exception e) {
				response.put("mensaje", "Error al leer la el archivo cargado");
				response.put("Error", e.getMessage() + " " + e.getMessage());
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		response.put("mensaje", "Uploaded the file successfully: " + file.getOriginalFilename());
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@GetMapping()
	public ResponseEntity<?> listStudents(){

		List<Estudiante> lista = objEstService.listStudents();
		ResponseEntity<?> objRespuesta;

		HashMap<String, Object> respuestas = new HashMap();
		if(lista.isEmpty()){
			respuestas.put("mensaje", "No existen estudiantes almacenados en la base de datos");
			objRespuesta= new ResponseEntity<HashMap<String, Object>>(respuestas,HttpStatus.NO_CONTENT);
		}    else
		{
			objRespuesta= new ResponseEntity<List<Estudiante>>(lista,HttpStatus.OK);
		}
		return objRespuesta;

	}
	@DeleteMapping("/{id}")
	public  ResponseEntity<?> deleteStudent(@PathVariable Long id){
		Estudiante objEstudiante = objEstService.dataStudent(id).orElse(null);
		if(objEstudiante != null){
			objEstService.delete(objEstudiante);
			return new ResponseEntity<>(objEstService.dataStudent(id),HttpStatus.OK);
		}else{

			HashMap<String,String> respuesta = new HashMap<>();
			respuesta.put("mensaje", "no se encontro el estudiante ");
			return new ResponseEntity<>(respuesta,HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> studentdata(@PathVariable Long id){
		try {
			return new ResponseEntity<>(objEstService.dataStudent(id),HttpStatus.OK);
		} catch (Exception e) {
			HashMap<String,String> respuesta = new HashMap<>();
			respuesta.put("mensaje", "no se encontro el estudiante ");
			return new ResponseEntity<>(respuesta,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
	@GetMapping("/{id}/estados")
	public ResponseEntity<?> obtenerEstadosPorEstudiante(@PathVariable(name = "id") Long idEstudiante,
														 @RequestParam(name = "pageNumber") int pageNumber,
														 @RequestParam(name = "pageSize") int pageSize) {
		try {
			PageRequest pageRequest =PageRequest.of(pageNumber, pageSize);
			if (estadoService.obtenerEstadosPorEstudiante(idEstudiante, pageRequest).isEmpty()) {
				HashMap<String, String> respuesta = new HashMap<>();
				respuesta.put("Mensaje", "El estudiante no tiene estados asociados");
				return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
			}
			{
				Page<Estados> pageResultEstados = estadoService.obtenerEstadosPorEstudiante(idEstudiante, pageRequest);
				List<Estados> estados = pageResultEstados.getContent();
				return new ResponseEntity<>(new PageImpl<>(estados, pageRequest, pageResultEstados.getTotalElements()), HttpStatus.OK);
			}
		} catch (Exception e) {
			HashMap<String, String> respuesta = new HashMap<>();
			respuesta.put("Mensaje", "Error: " + e.getMessage());
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		}
	}

	@GetMapping("/{id}/estadosSinPaginacion")
	public ResponseEntity<?> obtenerEstadosPorEstudiante(@PathVariable(name = "id") Long idEstudiante) {
		try {
			if (estadoService.obtenerEstadosPorEstudianteSinPaginacion(idEstudiante).isEmpty()) {
				HashMap<String, String> respuesta = new HashMap<>();
				respuesta.put("Mensaje", "El estudiante no tiene estados asociados");
				return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
			}
			{
				List<Estados> estados = estadoService.obtenerEstadosPorEstudianteSinPaginacion(idEstudiante);
				return new ResponseEntity<>( estados , HttpStatus.OK);
			}
		} catch (Exception e) {
			HashMap<String, String> respuesta = new HashMap<>();
			respuesta.put("Mensaje", "Error: " + e.getMessage());
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		}
	}

}
