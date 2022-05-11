package co.edu.unicauca.asae.proyecto.maestriacomputacion.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Estudiante;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Reingreso;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.services.IReingresoService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ReingresoController {
	@Autowired
	private IReingresoService reingresoService;

	@GetMapping("/reingresos/page/{page}")
	public Page<Reingreso> index(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 2);
		return reingresoService.findAll(pageable);
	}

	@GetMapping("/reingresos")
	public List<Reingreso> index() {
		return reingresoService.findAll();
	}

	@GetMapping("/reingresos/estudiante/{id}/{page}")
	public Page<Reingreso> reentryByStudent(@PathVariable String id, @PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 2);
		return reingresoService.findByObjEstudiante(id, pageable);
	}

	/*
	 * @GetMapping("/reingresos/estudiante/{id}")
	 * public List<Reingreso> reentryByStudent(@PathVariable Long id) {
	 * Estudiante objEstudiante = new Estudiante();
	 * objEstudiante.setId(id);
	 * return reingresoService.findByObjEstudiante(objEstudiante);
	 * }
	 */

	@DeleteMapping("/reingresos/{rein_id}")
	public ResponseEntity<?> delete(@PathVariable Integer rein_id) {
		Map<String, Object> response = new HashMap<>();

		try {
			reingresoService.delete(rein_id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el reingreso de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El reingreso se eliminó con éxito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@PostMapping("/reingresos")
	public ResponseEntity<?> create(@Valid @RequestBody Reingreso reingreso, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		Reingreso reingresoCreado;

		if (result.hasErrors()) {

			List<String> listaErrores = new ArrayList<>();

			for (FieldError error : result.getFieldErrors()) {
				listaErrores.add("Campo '" + error.getField() + "‘ " + error.getDefaultMessage());
			}

			response.put("errors", listaErrores);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {

			reingresoCreado = reingresoService.save(reingreso);

		} catch (DataIntegrityViolationException e) {

			response.put("mensaje", "El documento con id " + reingreso.getObjDocumentoOtro().getDocId()
					+ " ya se encuentra asociado a otro estudiante");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al crear el reingreso en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El reingreso se ha creado con éxito!");
		response.put("reingreso", reingresoCreado);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

	@PutMapping("/reingresos/{rein_id}")
	public ResponseEntity<?> update(@Valid @RequestBody Reingreso reingreso, BindingResult result,
			@PathVariable Integer rein_id) {
		Reingreso reingresoActual = reingresoService.findById(rein_id);

		Reingreso reingresoActualizado = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {

			List<String> listaErrores = new ArrayList<>();

			for (FieldError error : result.getFieldErrors()) {
				listaErrores.add("Campo '" + error.getField() + "‘ " + error.getDefaultMessage());
			}

			response.put("errors", listaErrores);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);

		}

		if (reingresoActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el reingreso con ID: "
					.concat(rein_id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			reingresoActual.setFechaIngreso(reingreso.getFechaIngreso());
			reingresoActual.setNumResolucion(reingreso.getNumResolucion());
			reingresoActual.setSemestreFinanciero(reingreso.getSemestreFinanciero());
			reingresoActual.setSemestreAcademico(reingreso.getSemestreAcademico());
			reingresoActual.setObjEstudiante(reingreso.getObjEstudiante());
			reingresoActual.setObjDocumentoOtro(reingreso.getObjDocumentoOtro());

			reingresoActualizado = reingresoService.save(reingresoActual);

		} catch (DataIntegrityViolationException e) {

			response.put("mensaje", "El documento con id " + reingreso.getObjDocumentoOtro().getDocId()
					+ " ya se encuentra asociado a otro estudiante");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el reingreso en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El reingreso ha sido actualizado con éxito!");
		response.put("reingreso", reingresoActualizado);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
