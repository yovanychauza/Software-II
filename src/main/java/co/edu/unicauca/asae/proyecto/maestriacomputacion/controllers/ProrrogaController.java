package co.edu.unicauca.asae.proyecto.maestriacomputacion.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Prorroga;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.services.IEstudiante;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.services.IProrrogaService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ProrrogaController {
    @Autowired
    private IEstudiante estudianteService;

    @Autowired
    private IProrrogaService prorrogaService;

    @GetMapping("/prorrogas/page/{page}")
    public Page<Prorroga> listarProrrogas(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 4);
        return this.prorrogaService.findAll(pageable);
    }

    @GetMapping("/estudiantes/{idEstudiante}/prorrogas/page/{page}")
    public ResponseEntity<?> prorrogasPorEstudiante(@PathVariable("idEstudiante") Long idEstudiante, @PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 4);
        Map<String, Object> respuesta = new HashMap<>();
        try {
            Optional<Estudiante> estudiante = this.estudianteService.dataStudent(idEstudiante);
            if (estudiante.isPresent()) {
                Page<Prorroga> prorrogas = this.prorrogaService.prorrogasEstudiante(idEstudiante, pageable);
                return new ResponseEntity<>(prorrogas, HttpStatus.OK);
            } else {
                respuesta.put("mensaje", "No se encontró el estudiante con la identificación ingresada");
                return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException dae) {
            respuesta.put("mensaje", "Error al realizar la consulta en la base de datos");
            respuesta.put("Error", dae.getMessage() + " " + dae.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/estudiantes/{idEstudiante}/prorrogas/crear")
    public ResponseEntity<?> crearProrroga(@Valid @PathVariable("idEstudiante") Long idEstudiante,
    @Valid @RequestBody Prorroga prorroga, BindingResult result) {
        Map<String, Object> respuestas = new HashMap<>();
        Prorroga prorroga_guardada = null;
        ResponseEntity<?> obj_respuesta = null;

        // Mostrar errores
        if (result.hasErrors()) {
            List<String> listaErrores = new ArrayList<String>();
            for (FieldError error : result.getFieldErrors()) {
                listaErrores.add("Campo " + error.getField() + ": " + error.getDefaultMessage());
            }
            respuestas.put("errors", listaErrores);
            return new ResponseEntity<Map<String, Object>>(respuestas, HttpStatus.BAD_REQUEST);
        }

        try {
            prorroga_guardada = prorrogaService.save(prorroga);
            obj_respuesta = new ResponseEntity<>(prorroga_guardada, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            respuestas.put("mensaje", "El documento con id " + prorroga.getDocumento().getDocId()
                    + " ya se encuentra asociado a otro estudiante");
            respuestas.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(respuestas, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataAccessException e) {
            respuestas.put("Error", "Error al realizar el registro en la base de datos");
            respuestas.put("Descripción", e.getMessage() + " " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(respuestas, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return obj_respuesta;
    }

    @GetMapping("prorrogas/listar/{id}")
    public ResponseEntity<?> prorrogaPorId(@PathVariable("id") Long parIdProrroga) {
        Map<String, Object> respuesta = new HashMap<>();
        try {
            Optional<Prorroga> prorroga = this.prorrogaService.findById(parIdProrroga);
            if (prorroga.isPresent()) {
                return new ResponseEntity<>(prorroga.get(), HttpStatus.OK);
            } else {
                respuesta.put("mensaje", "No se encontró la prorroga con la identificación ingresada");
                return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException dae) {
            respuesta.put("mensaje", "Error al realizar la consulta en la base de datos");
            respuesta.put("Error", dae.getMessage() + " " + dae.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/estudiantes/{idEstudiante}/prorrogas/actualizar/{id}")
    public ResponseEntity<?> updateProrroga(@Valid @PathVariable("idEstudiante") Long idEstudiante,
            @RequestBody Prorroga prorroga, BindingResult result,
            @PathVariable Long id) {
        Map<String, Object> respuestas = new HashMap<>();
        Prorroga prorroga_actualizada = null;
        Optional<Prorroga> prorroga_actual = null;
        Optional<Estudiante> estudiante_actual = null;

        ResponseEntity<?> obj_respuesta = null;

        // Mostrar errores
        if (result.hasErrors()) {
            List<String> listaErrores = new ArrayList<String>();
            for (FieldError error : result.getFieldErrors()) {
                listaErrores.add("Campo " + error.getField() + ": " + error.getDefaultMessage());
            }
            respuestas.put("errors", listaErrores);
            return new ResponseEntity<Map<String, Object>>(respuestas, HttpStatus.BAD_REQUEST);
        }

        try {
            estudiante_actual = this.estudianteService.dataStudent(idEstudiante);
            if (!estudiante_actual.isPresent()) {
                respuestas.put("mensaje", "El estudiante con ID: " + idEstudiante + " no existe en la base de datos!");
                return new ResponseEntity<Map<String, Object>>(respuestas, HttpStatus.NOT_FOUND);
            } else {
                prorroga_actual = this.prorrogaService.findById(id);
                if (!prorroga_actual.isPresent()) {
                    respuestas.put("mensaje", "La prorroga con ID: " + id + " no existe en la base de datos!");
                    return new ResponseEntity<Map<String, Object>>(respuestas, HttpStatus.NOT_FOUND);
                } else {
                    prorroga_actualizada = this.prorrogaService.save(prorroga);
                    if (prorroga_actualizada != null) {
                        obj_respuesta = new ResponseEntity<>(prorroga_actualizada, HttpStatus.OK);
                    } else {
                        respuestas.put("Error", "Error al guardar en la base de datos");
                    }
                }
            }
        } catch (DataAccessException e) {
            respuestas.put("Error", "Error al realizar el registro en la base de datos");
            respuestas.put("Descripción", e.getMessage() + " " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(respuestas, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return obj_respuesta;
    }

    @DeleteMapping("/estudiantes/{idEstudiante}/prorrogas/eliminar/{id}")
    public ResponseEntity<?> deleteProrroga(@Valid @PathVariable("idEstudiante") Long idEstudiante,
            @PathVariable Long id) {
        ResponseEntity<?> obj_respuesta = null;
        Map<String, Object> respuestas = new HashMap<>();

        Optional<Estudiante> estudiante_actual = null;
        Optional<Prorroga> prorroga_actual = null;
        Prorroga prorroga_eliminada = null;

        try {
            estudiante_actual = this.estudianteService.dataStudent(idEstudiante);

            if (!estudiante_actual.isPresent()) {
                respuestas.put("Error", "No se ha encontrado la identificación de la prorroga");
                return new ResponseEntity<Map<String, Object>>(respuestas, HttpStatus.NOT_FOUND);
            } else {
                prorroga_actual = this.prorrogaService.findById(id);
                if (!estudiante_actual.isPresent()) {
                    respuestas.put("Error", "No se ha encontrado la identificación de la prorroga");
                    return new ResponseEntity<Map<String, Object>>(respuestas, HttpStatus.NOT_FOUND);
                } else {
                    prorroga_eliminada = this.prorrogaService.deleteProrroga(id);
                    obj_respuesta = new ResponseEntity<>(estudiante_actual.get().getProrrogas(), HttpStatus.OK);
                }
            }
        } catch (DataAccessException e) {
            respuestas.put("Error", "Error al realizar el registro en la base de datos");
            respuestas.put("Descripción", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(respuestas, HttpStatus.BAD_REQUEST);
        }
        return obj_respuesta;
    }

}
