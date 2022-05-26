package co.edu.unicauca.asae.proyecto.maestriacomputacion.controllers;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Docente;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.response.AsignaturaResponseRest;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.response.DocenteResponseRest;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.services.IDocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class DocenteController {

    @Autowired
    private IDocenteService docenteService;
	@GetMapping("/docentes")
	public List<Docente> index() {
		System.out.println("sdsds");
		return docenteService.findAll();
	}

	@PostMapping("/docentes")	
	public ResponseEntity<?>  create(@Valid @RequestBody Docente docente, BindingResult result) {
		
		Map<String, Object> response = new HashMap<>();
		Docente objDocente;
				
		if(result.hasErrors())
		{

			List<String> listaErrores= new ArrayList<>();

			for (FieldError error : result.getFieldErrors()) {
				listaErrores.add("El campo '" + error.getField() +"‘ "+ error.getDefaultMessage());
			}

			response.put("errors", listaErrores);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);

		}

		
		try {
			objDocente=this.docenteService.save(docente);
		} 
		catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la inserción en la base de datos");
			response.put("error", e.getMessage() + "" + e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Docente>(objDocente, HttpStatus.OK);

	}


    @PostMapping("/docentes/crearDocentesExcel")
    public ResponseEntity<DocenteResponseRest> crearDocentesExcel(@RequestParam("file") MultipartFile file){
        ResponseEntity<DocenteResponseRest> response = docenteService.crearDocentesPorDocumentos(file);
        return response;
    }
    @GetMapping("/docentes/{id}")
	public ResponseEntity<?>  show(@PathVariable Long id) {	
		Docente cliente = new Docente();
		Map<String, Object> response = new HashMap<>();

		try {
			cliente = docenteService.findById(id);
		} 
		catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage() + "" + e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if(cliente == null) {
			response.put("mensaje", "El docente ID: " + id + " no existe en la base de datos!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Docente>(cliente, HttpStatus.OK);
	
	}
    @PutMapping("/docentes/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Docente cliente, BindingResult result, @PathVariable Long id) {

		Docente clienteActual = docenteService.findById(id);
		System.out.println("aqui");

		Docente clienteUpdated = null;

		Map<String, Object> response = new HashMap<>();

		
		if(result.hasErrors())
		{

			List<String> listaErrores= new ArrayList<>();

			for (FieldError error : result.getFieldErrors()) {
				listaErrores.add("El campo '" + error.getField() +"‘ "+ error.getDefaultMessage());
			}

			response.put("errors", listaErrores);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);

		}
		
						
		if (clienteActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el docente ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			
			clienteActual.setIdentificacion(cliente.getIdentificacion());
			clienteActual.setTipoIdentificacion(cliente.getTipoIdentificacion());
            clienteActual.setApellidos(cliente.getApellidos());
			clienteActual.setNombres(cliente.getNombres());
            clienteActual.setGenero(cliente.getGenero());
            clienteActual.setTelefono(cliente.getTelefono());
			clienteActual.setCorreo(cliente.getCorreo());
            clienteActual.setTitulo(cliente.getTitulo());
            clienteActual.setAbreviatura(cliente.getAbreviatura());
            clienteActual.setUniversidadTitulo(cliente.getUniversidadTitulo());
            clienteActual.setCategoriaMic(cliente.getCategoriaMic());
            clienteActual.setLincVlac(cliente.getLincVlac());
            clienteActual.setIdFacultad(cliente.getIdFacultad());
            clienteActual.setDepartamento(cliente.getDepartamento());
            clienteActual.setGrupoInv(cliente.getGrupoInv());
            clienteActual.setLineaInv(cliente.getLineaInv());
            clienteActual.setTipoVinculacion(cliente.getTipoVinculacion());
            clienteActual.setEscalafon(cliente.getEscalafon());
			clienteActual.setObservacion(cliente.getObservacion());

			clienteUpdated = docenteService.save(clienteActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el docente en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El docente ha sido actualizado con éxito!");
		response.put("docente", clienteUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
    @DeleteMapping("/docentes/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
		    docenteService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el docente de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El docente ha sido eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

}
