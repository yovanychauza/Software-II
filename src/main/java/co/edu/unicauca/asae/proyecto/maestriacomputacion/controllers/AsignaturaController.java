package co.edu.unicauca.asae.proyecto.maestriacomputacion.controllers;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Acta;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Asignatura;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.response.AsignaturaResponseRest;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.services.IAsignaturaService;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@CrossOrigin (origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/asignatura")
@Validated
public class AsignaturaController {

	private static final Logger log = LoggerFactory.getLogger(AsignaturaController.class);
	
	@Autowired
	private IAsignaturaService asignaturaService;
	
	@GetMapping("/busquedaVariada/{busqueda}")
	public ResponseEntity<List<Asignatura>> busquedaVariada(@PathVariable String busqueda){
		return this.asignaturaService.findByCodigoOrNombreOrEstado(busqueda);
	}
	
	@PutMapping("/actualizar/{codigo}")
	public ResponseEntity<?> actualizarAsignatura(@Valid @RequestBody Asignatura asignatura, 
			@Size(min = 2, max = 45) @PathVariable String codigo){
		HashMap<String, Object> respuestas = new HashMap<>();
		ResponseEntity<?> respuesta = new ResponseEntity<>(
				"No hay ninguna asignatura registrada", 
				HttpStatus.NOT_FOUND);
		Asignatura asignaturaDB = null;
		try {
			List<Integer> actasIds = new ArrayList<>();
			if (asignatura.getActas() != null) {
				actasIds = asignatura.getActas().stream().map(Acta::getIdActa).collect(Collectors.toList());
				asignatura.getActas().clear();
			}
			asignaturaDB = this.asignaturaService.update(asignatura, codigo);
			this.actualizarActasAsignatura(asignatura.getCodigo(), actasIds);
			
			if (asignaturaDB != null) {
				respuesta = new ResponseEntity<Asignatura>(asignaturaDB,HttpStatus.OK);
			}else {
				respuestas.put("mensaje", "La Asignatura con Codigo: "+codigo+" no existe en la base de datos");
				respuesta = new ResponseEntity<HashMap<String, Object>>(respuestas,HttpStatus.NOT_FOUND);
			}
			
		} catch (DataAccessException e) {
			respuestas.put("mensaje", "Error al realizar la consulta en la base de datos");
            respuestas.put("descripción del error", e.getMessage());
            respuesta= new ResponseEntity<HashMap<String, Object>>(respuestas,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;		
	}

	@DeleteMapping("/eliminar/{codigo}")
	public ResponseEntity<Map<String, String>> deactivateAsignatura(@PathVariable @NotNull String codigo){
		Map<String, String> responseDetail = new HashMap<>();
		try {
			Optional<Asignatura> optAsignatura = this.asignaturaService.deactivateAsignatura(codigo);

			return optAsignatura.map(asignatura -> {
				responseDetail.put("mensaje", "La asignatura '" + optAsignatura.get().getNombre() + "' se ha eliminado correctamente.");
				return new ResponseEntity<>(responseDetail, HttpStatus.OK);
			}).orElseGet(() -> {
				responseDetail.put("mensaje", "La asignatura no se encuentra registrada o está inactiva.");
				return new ResponseEntity<>(responseDetail, HttpStatus.NOT_FOUND);
			});
		} catch (Exception e) {
			responseDetail.put("mensaje", "Ha ocurrido un error interno en el servidor.");
			log.error("Error: {}", e.getMessage(), e);
			return new ResponseEntity<>(responseDetail, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/listar/nombre/{nombre}")
	public ResponseEntity<?> listarAsignaturasNombre(@PathVariable String nombre){
		ResponseEntity<?> respuesta = new ResponseEntity<>(
				"No hay ninguna asignatura registrada con ese nombre", 
				HttpStatus.NOT_FOUND);
		List<Asignatura> asignaturas = this.asignaturaService.findByNombre(nombre);
		if (!asignaturas.isEmpty()) {
			respuesta = new ResponseEntity<>(asignaturas, HttpStatus.OK);
		}
		return respuesta;
	}
	
	@GetMapping("/listar/estado/{estado}")
	public ResponseEntity<?> listarAsignaturasEstado(@PathVariable String estado){
		ResponseEntity<?> respuesta = new ResponseEntity<>(
				"No hay ninguna asignatura registrada con ese estado", 
				HttpStatus.NOT_FOUND);
		List<Asignatura> asignaturas = this.asignaturaService.findByEstado(estado);
		if (!asignaturas.isEmpty()) {
			respuesta = new ResponseEntity<>(asignaturas, HttpStatus.OK);
		}
		return respuesta;
	}
	
	@GetMapping("/listar/fecha-aprobacion/{fechaAprobacion}")
	public ResponseEntity<?> listarAsignaturasFechaAprobacion(@PathVariable String fechaAprobacion) throws ParseException{
		ResponseEntity<?> respuesta = new ResponseEntity<>(
				"No hay ninguna asignatura registrada en esa fecha de aprobacion", 
				HttpStatus.NOT_FOUND);
		System.out.println("ACA ESTA LA FECHA---->>>>> " + fechaAprobacion);
		Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaAprobacion);
		List<Asignatura> asignaturas = this.asignaturaService.findByFechaAprobacion(fecha);
		if (!asignaturas.isEmpty()) {
			respuesta = new ResponseEntity<>(asignaturas, HttpStatus.OK);
		}
		return respuesta;
	}
	
	@GetMapping("/listar/area-inv/{areaInvestigacion}")
	public ResponseEntity<?> listarAsignaturasAreaInvestigacion(@PathVariable String areaInvestigacion){
		ResponseEntity<?> respuesta = new ResponseEntity<>(
				"No hay ninguna asignatura registrada con esa area de investigacion", 
				HttpStatus.NOT_FOUND);
		List<Asignatura> asignaturas = this.asignaturaService.findByAreaInvestigacion(areaInvestigacion);
		if (!asignaturas.isEmpty()) {
			respuesta = new ResponseEntity<>(asignaturas, HttpStatus.OK);
		}
		return respuesta;
	}
	
	@GetMapping("/listar/linea-inv/{lineaInvestigacion}")
	public ResponseEntity<?> listarAsignaturasLineaInvestigacion(@PathVariable String lineaInvestigacion){
		ResponseEntity<?> respuesta = new ResponseEntity<>(
				"No hay ninguna asignatura asociada con esa linea de investigacion", 
				HttpStatus.NOT_FOUND);
		List<Asignatura> asignaturas = this.asignaturaService.findByLineaInvestigacion(lineaInvestigacion);
		if (!asignaturas.isEmpty()) {
			respuesta = new ResponseEntity<>(asignaturas, HttpStatus.OK);
		}
		return respuesta;
	}
	
	@GetMapping("/listar/tipo/{tipo}")
	public ResponseEntity<?> listarAsignaturasTipo(@PathVariable String tipo){
		ResponseEntity<?> respuesta = new ResponseEntity<>(
				"No hay ninguna asignatura registrada con ese tipo", 
				HttpStatus.NOT_FOUND);
		List<Asignatura> asignaturas = this.asignaturaService.findByTipo(tipo);
		if (!asignaturas.isEmpty()) {
			respuesta = new ResponseEntity<>(asignaturas, HttpStatus.OK);
		}
		return respuesta;
	}
	
	@GetMapping("/listar/num-creditos/{numeroCreditos}")
	public ResponseEntity<?> listarAsignaturasNumeroCreditos(@PathVariable Integer numeroCreditos){
		ResponseEntity<?> respuesta = new ResponseEntity<>(
				"No hay ninguna asignatura registrada con ese numero de creditos", 
				HttpStatus.NOT_FOUND);
		List<Asignatura> asignaturas = this.asignaturaService.findByNumeroCreditos(numeroCreditos);
		if (!asignaturas.isEmpty()) {
			respuesta = new ResponseEntity<>(asignaturas, HttpStatus.OK);
		}
		return respuesta;
	}
	
	@GetMapping("/listar/horas-pres/{horasPresenciales}")
	public ResponseEntity<?> listarAsignaturasHorasPresenciales(@PathVariable Integer horasPresenciales){
		ResponseEntity<?> respuesta = new ResponseEntity<>(
				"No hay ninguna asignatura registrada con ese numero de horas presenciales", 
				HttpStatus.NOT_FOUND);
		List<Asignatura> asignaturas = this.asignaturaService.findByHorasPresenciales(horasPresenciales);
		if (!asignaturas.isEmpty()) {
			respuesta = new ResponseEntity<>(asignaturas, HttpStatus.OK);
		}
		return respuesta;
	}
	
	@GetMapping("/listar/horas-no-pres/{horasNoPresenciales}")
	public ResponseEntity<?> listarAsignaturasHorasNoPresenciales(@PathVariable Integer horasNoPresenciales){
		ResponseEntity<?> respuesta = new ResponseEntity<>(
				"No hay ninguna asignatura registrada con ese numero de horas no presenciales", 
				HttpStatus.NOT_FOUND);
		List<Asignatura> asignaturas = this.asignaturaService.findByHorasNoPresenciales(horasNoPresenciales);
		if (!asignaturas.isEmpty()) {
			respuesta = new ResponseEntity<>(asignaturas, HttpStatus.OK);
		}
		return respuesta;
	}

	@GetMapping("/consulta/codigo/{codigo}")
	public ResponseEntity<?> informacionAsignatura(@PathVariable String codigo){
		ResponseEntity<?> respuesta = new ResponseEntity<>(
				"No hay ninguna asignatura con el ese codigo", 
				HttpStatus.NOT_FOUND);
				Asignatura asignatura=null;
				asignatura = this.asignaturaService.findById(codigo);
		if (asignatura!=null) {
			respuesta = new ResponseEntity<Asignatura>(asignatura, HttpStatus.OK);
		}
		return respuesta;
	}

	@GetMapping("/consulta/codigoList/{codigo}")
	public ResponseEntity<?> informacionAsignaturaCodigo(@PathVariable String codigo){
		ResponseEntity<?> respuesta = new ResponseEntity<>(
				"No hay ninguna asignatura con ese codigo", 
				HttpStatus.NOT_FOUND);
		Asignatura asignatura=null;
		asignatura = this.asignaturaService.findById(codigo);
		List<Asignatura> lista = new ArrayList<>();
		if (asignatura!=null) {
			lista.add(asignatura);
			respuesta = new ResponseEntity<List<Asignatura>>(lista, HttpStatus.OK);
		}
		return respuesta;
	}

	@PostMapping("/crearAsignatura")
	public ResponseEntity<AsignaturaResponseRest> crearAsignatura(@Valid @RequestBody Asignatura asignatura){
		List<Integer> actasIds = new ArrayList<>();
		if (asignatura.getActas() != null) {
			actasIds = asignatura.getActas().stream().map(Acta::getIdActa).collect(Collectors.toList());
			asignatura.getActas().clear();
		}
		ResponseEntity<AsignaturaResponseRest> response = asignaturaService.guardarAsignatura(asignatura);
		this.actualizarActasAsignatura(asignatura.getCodigo(), actasIds);
		
		return response;
	}
	
	@PostMapping("/cargarAsignaturasExcel")
	public ResponseEntity<AsignaturaResponseRest> cargarAsignaturasExcel(@RequestParam("file") MultipartFile file){
		ResponseEntity<AsignaturaResponseRest> response = asignaturaService.cargarAsignaturasExcel(file);
		return response;
	}

	@PostMapping("/asociar/acta/{numeroActa}/{codigo}")
    public ResponseEntity<AsignaturaResponseRest> asociarActaAsignatura(@PathVariable Integer numeroActa, @PathVariable String codigo) {
		ResponseEntity<AsignaturaResponseRest> response = this.asignaturaService.asociarActaAsignatura(numeroActa, codigo);
	   	return response;
    }
	@PutMapping("/actualizarActas/{codigoAsignatura}")
	public ResponseEntity<Asignatura> actualizarActasAsignatura(@PathVariable String codigoAsignatura, @RequestBody List<Integer> astasIds) {
		Asignatura updated = this.asignaturaService.actualizarActasAsignatura(codigoAsignatura, astasIds);
		if (updated != null) {
			return ResponseEntity.status(HttpStatus.OK).body(updated);
		} else {
			return ResponseEntity.notFound().build();
		}
	}


	@GetMapping("/listar/actas/de/{codigo}/{page}")
	public ResponseEntity<Page<Acta>> listarActas(@PathVariable String codigo, @PathVariable Integer page){
		try {
			return new ResponseEntity(this.asignaturaService.findByAsignatura(codigo, page), HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

	}

	@ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("nombre del método y parametros erroneos: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, Object> response = new HashMap<>();
		
		List<String> listaErrores = new ArrayList<>();
		
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			listaErrores.add("El campo '" + error.getField() +"‘ "+ error.getDefaultMessage());
		}
		response.put("errors", listaErrores);
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/listarAsignaturas")
	public  ResponseEntity<?> listAsignaturas(){
		List<Asignatura> lista = asignaturaService.findAllAsignatura();
		ResponseEntity<?> objRespuesta;

		HashMap<String, Object> respuestas = new HashMap<>();
		if(lista.isEmpty()){
			respuestas.put("mensaje", "No existen asignaturas almacenadas en la base de datos");
			objRespuesta= new ResponseEntity<HashMap<String, Object>>(respuestas,HttpStatus.NO_CONTENT);
		}    else
		{
			objRespuesta= new ResponseEntity<List<Asignatura>>(lista,HttpStatus.OK);
		}
		return objRespuesta;
	}

}
