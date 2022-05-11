package co.edu.unicauca.asae.proyecto.maestriacomputacion.controllers;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.*;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.exception.DeleteActaException;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.services.IDocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.classic.pattern.NamedConverter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api/documentos")
public class DocumentRestController {

    private static final Logger log = LoggerFactory.getLogger(DocumentRestController.class);

    @Autowired
    private IDocumentService documentService;

    // David Section
    @GetMapping("/list/{type}/{id}")
    public ResponseEntity<?> getDocument(@PathVariable String type, @PathVariable Integer id) {
        ResponseEntity<?> response = new ResponseEntity<>("No hay ningun documento registrado con el id " + id,
                HttpStatus.NOT_FOUND);
        Document document = null;
        try {
            document = this.documentService.getDocument(type, id);
            if (document != null) {
                response = new ResponseEntity<>(document, HttpStatus.OK);
            }
        } catch (Exception e) {
            response = new ResponseEntity<>(document, HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    @GetMapping("/others/{type}")
    public ResponseEntity<?> findDocumentByOtherType(@PathVariable String type) {
        ResponseEntity<?> response = new ResponseEntity<>(
                "No existen otros documentos de tipo " + type + ".", HttpStatus.NOT_FOUND);

        List<Document> documentList = this.documentService.findDocumentByType(type);
        if (!documentList.isEmpty()) {
            response = new ResponseEntity<>(documentList, HttpStatus.OK);
        }
        return response;
    }

    @GetMapping("list/others/{criterion}")
    public ResponseEntity<?> findDocumentByNameOrDescription(@PathVariable String criterion) {
        ResponseEntity<?> response = new ResponseEntity<>(
                "No existen documentos con nombre o descripcion " + criterion + ".", HttpStatus.NOT_FOUND);

        List<Document> documentList = this.documentService.findDocumentByCriterion(criterion);
        if (!documentList.isEmpty()) {
            response = new ResponseEntity<>(documentList, HttpStatus.OK);
        }
        return response;
    }

    @GetMapping("/find/{typeDocument}/{dateStart}/{dateEnd}")
    public ResponseEntity<?> findDocumentByDate(@PathVariable String typeDocument, @PathVariable String dateStart,
            @PathVariable String dateEnd) throws ParseException {
        ResponseEntity<?> response = new ResponseEntity<>("No existen documentos en el rango de fecha proporcionado ",
                HttpStatus.NOT_FOUND);
        List<Document> documents = this.documentService.findDocumentByDate(typeDocument, dateStart, dateEnd);
        if (!documents.isEmpty()) {
            response = new ResponseEntity<>(documents, HttpStatus.OK);
        }
        return response;

    }

    // End David section

    @GetMapping("/list/page/{typedocument}/{page}")
    public ResponseEntity<?> listarDocumentos(@PathVariable String typedocument, @PathVariable Integer page) {
        ResponseEntity<?> respuesta = new ResponseEntity<>(
                "No hay ningun documento registrado",
                HttpStatus.NOT_FOUND);
        Pageable pageable = PageRequest.of(page,2);
        Page<Document> documentos = this.documentService.getDocuments(typedocument,pageable);
        if (!documentos.isEmpty()) {
            respuesta = new ResponseEntity<>(documentos, HttpStatus.OK);
        }
        return respuesta;
    }

    @PostMapping("/crear/oficio")
    public ResponseEntity<?> crearOficio(@Valid @RequestBody Oficio oficio, BindingResult result) {

        Oficio objOficio = null;
        HashMap<String, Object> respuestas = new HashMap<>();
        ResponseEntity<?> objRespuesta;

        if (result.hasErrors()) {
            List<String> listaErrores = new ArrayList<>();
            for (FieldError error : result.getFieldErrors()) {
                listaErrores.add("Campo " + error.getField() + ": " + error.getDefaultMessage());
            }
            respuestas.put("errors", listaErrores);
            return new ResponseEntity<Map<String, Object>>(respuestas, HttpStatus.BAD_REQUEST);
        }

        try {
            objOficio = documentService.saveOficio(oficio);
            objRespuesta = new ResponseEntity<Oficio>(objOficio, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            respuestas.put("mensaje", "Error al ingresar en la base de datos");
            respuestas.put("descripcion del error", e.getMessage());
            objRespuesta = new ResponseEntity<HashMap<String, Object>>(respuestas, HttpStatus.BAD_REQUEST);
        }
        return objRespuesta;
    }

    @PostMapping("/crear/upload/{tipo}")
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Integer id,
            @PathVariable String tipo) {
        HashMap<String, Object> responses = new HashMap<>();
        if (!archivo.isEmpty()) {
            String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
            Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();

            try {
                Files.copy(archivo.getInputStream(), rutaArchivo);
            } catch (IOException e) {
                responses.put("mensaje", "Error al subir el documento " + nombreArchivo);
                responses.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Document objDocActualizado = null;
            Document objDocRecuperado = null;

            objDocRecuperado = this.documentService.getDocument(tipo, id);
            String nombreArchivoAnterior = objDocRecuperado.getArchivo();
            eliminarArchivo(nombreArchivoAnterior);
            objDocRecuperado.setArchivo(nombreArchivo);
            if (tipo.equalsIgnoreCase("ACTA")) {
                objDocActualizado = this.documentService.saveActa((Acta) objDocRecuperado);
            } else if (tipo.equalsIgnoreCase("OFICIO")) {
                objDocActualizado = this.documentService.saveOficio((Oficio) objDocRecuperado);
            } else {
                objDocActualizado = this.documentService.saveDocumentoOtro((OtherDocument) objDocRecuperado);
            }
            return new ResponseEntity<Document>(objDocActualizado, HttpStatus.CREATED);

        } else {

            return new ResponseEntity<HashMap<String, Object>>(responses, HttpStatus.BAD_REQUEST);
        }
    }

    private void eliminarArchivo(String nombreArchivoAnterior) {
        if (nombreArchivoAnterior != null && nombreArchivoAnterior.length() > 0) {
            Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreArchivoAnterior).toAbsolutePath();
            File archivoAnterior = rutaFotoAnterior.toFile();
            if (archivoAnterior.exists() && archivoAnterior.canRead()) {
                archivoAnterior.delete();
            }
        }
    }

    @PostMapping("/crear/acta")
    public ResponseEntity<?> crearActa(@Valid @RequestBody Acta acta, BindingResult result) {
        Acta objActa = null;
        HashMap<String, Object> responses = new HashMap<>();
        ResponseEntity<?> response;
        if (result.hasErrors()) {
            List<String> listaErrores = new ArrayList<>();
            for (FieldError error : result.getFieldErrors()) {
                listaErrores.add("Campo " + error.getField() + ": " + error.getDefaultMessage());
            }
            responses.put("errors", listaErrores);
            return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.BAD_REQUEST);
        }
        try {
            objActa = documentService.saveActa(acta);
            response = new ResponseEntity<Acta>(objActa, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            responses.put("mensaje", "Error al ingresar en la base de datos");
            responses.put("descripcion del error", e.getMessage());
            response = new ResponseEntity<HashMap<String, Object>>(responses, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @DeleteMapping("/acta/eliminar/{idActa}")
    public ResponseEntity<?> deleteActa(@NotNull @PathVariable Integer idActa) {
        HashMap<String, Object> responseDetail = new HashMap<>();

        try {
            Acta foundActa = this.documentService.deleteActa(idActa);

            if (foundActa == null) {
                responseDetail.put("mensaje", "El ácta con código [" + idActa + "] no se encuentra registrada.");
                return new ResponseEntity<>(responseDetail, HttpStatus.NOT_FOUND);
            } else {
                responseDetail.put("mensaje", "El ácta [" + foundActa.getName() + "] se ha eliminado correctamente.");
                return new ResponseEntity<>(responseDetail, HttpStatus.OK);
            }
        } catch (DeleteActaException dae) {
            responseDetail.put("mensaje", dae.getMessage());
            log.error("Error al eliminar: {}", dae.getMessage(), dae);
            return new ResponseEntity<>(responseDetail, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            responseDetail.put("mensaje", "Ha ocurrido un error interno en el servidor.");
            log.error("Error: {}", e.getMessage(), e);
            return new ResponseEntity<>(responseDetail, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/crear/otro")
    public ResponseEntity<?> crearOtroDocumento(@Valid @RequestBody OtherDocument document, BindingResult result) {
        OtherDocument objDoc = null;
        HashMap<String, Object> responses = new HashMap<>();
        ResponseEntity<?> response;
        if (result.hasErrors()) {
            List<String> listaErrores = new ArrayList<>();
            for (FieldError error : result.getFieldErrors()) {
                listaErrores.add("Campo " + error.getField() + ": " + error.getDefaultMessage());
            }
            responses.put("errors", listaErrores);
            return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.BAD_REQUEST);
        }
        try {
            objDoc = documentService.saveDocumentoOtro(document);
            response = new ResponseEntity<OtherDocument>(objDoc, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            responses.put("mensaje", "Error al ingresar en la base de datos");
            responses.put("descripcion del error", e.getMessage());
            response = new ResponseEntity<HashMap<String, Object>>(responses, HttpStatus.BAD_REQUEST);
        }
        return response;

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @DeleteMapping("/eliminar/{typeDocument}/{codigo}")
    public ResponseEntity<?> eliminarDocumento(@PathVariable Integer codigo, @PathVariable String typeDocument) {
        HashMap<String, String> respuestas = new HashMap<>();
		ResponseEntity<?> respuesta = new ResponseEntity<>("No existe el documento que está solicitando", HttpStatus.NOT_FOUND);
		Document doc_eleminado = null;
		try {
			doc_eleminado = this.documentService.eliminarDocumento(codigo, typeDocument);
			if(doc_eleminado != null){
                String nombre_archivo = doc_eleminado.getArchivo();
                eliminarArchivo(nombre_archivo);
				respuesta = new ResponseEntity<Document>(doc_eleminado, HttpStatus.OK);
			}
		}catch (DataAccessException e) {
			respuestas.put("mensaje", "Error al eliminar el documento de la base de datos");
			respuestas.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			respuesta = new ResponseEntity<HashMap<String, String>>(respuestas, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch(Exception e){
			respuestas.put("\nMensaje", "Error al realizar la consulta en la base de datos");
            respuesta = new ResponseEntity<HashMap<String, String>>(respuestas,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return respuesta;
    }

    // Oscar
    @PutMapping("/actualizar/oficio/{codigo}")
    public ResponseEntity<?> actualizarOficio(@Valid @RequestBody Oficio oficio, @PathVariable Integer codigo,
            BindingResult result) {
        HashMap<String, Object> respuestas = new HashMap<>();
        ResponseEntity<?> respuesta = new ResponseEntity<>(
                "No hay ningun oficio registrado",
                HttpStatus.NOT_FOUND);
        if (result.hasErrors()) {
            List<String> listError = new ArrayList<>();
            for (FieldError error : result.getFieldErrors()) {
                listError.add("Campo " + error.getField() + ":  " + error.getDefaultMessage());

            }
            respuestas.put("error", listError);
            return new ResponseEntity<Map<String, Object>>(respuestas, HttpStatus.BAD_REQUEST);
        }
        Oficio oficioDB = null;
        try {

            oficioDB = this.documentService.updateOficio(oficio, codigo);

            if (oficioDB != null) {
                respuesta = new ResponseEntity<Oficio>(oficioDB, HttpStatus.OK);
            } else {
                respuestas.put("mensaje", "El oficio con codigo: " + codigo + " no existe en la base de datos");
                respuesta = new ResponseEntity<HashMap<String, Object>>(respuestas, HttpStatus.NOT_FOUND);
            }

        } catch (DataAccessException e) {
            respuestas.put("mensaje", "Error al realizar la consulta en la base de datos");
            respuestas.put("descripción del error", e.getMessage());
            respuesta = new ResponseEntity<HashMap<String, Object>>(respuestas, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }

    @PutMapping("/actualizar/acta/{codigo}")
    public ResponseEntity<?> actualizarActa(@Valid @RequestBody Acta acta, @PathVariable Integer codigo,
            BindingResult result) {
        HashMap<String, Object> respuestas = new HashMap<>();
        ResponseEntity<?> respuesta = new ResponseEntity<>(
                "No hay ninguna acta registrada",
                HttpStatus.NOT_FOUND);
        if (result.hasErrors()) {
            List<String> listError = new ArrayList<>();
            for (FieldError error : result.getFieldErrors()) {
                listError.add("Campo " + error.getField() + ":  " + error.getDefaultMessage());

            }
            respuestas.put("error", listError);
            return new ResponseEntity<Map<String, Object>>(respuestas, HttpStatus.BAD_REQUEST);
        }
        Acta actaDB = null;
        try {

            actaDB = this.documentService.updateActa(acta, codigo);

            if (actaDB != null) {
                respuesta = new ResponseEntity<Acta>(actaDB, HttpStatus.OK);
            } else {
                respuestas.put("mensaje", "La acta con codigo: " + codigo + " no existe en la base de datos");
                respuesta = new ResponseEntity<HashMap<String, Object>>(respuestas, HttpStatus.NOT_FOUND);
            }

        } catch (DataAccessException e) {
            respuestas.put("mensaje", "Error al realizar la consulta en la base de datos");
            respuestas.put("descripción del error", e.getMessage());
            respuesta = new ResponseEntity<HashMap<String, Object>>(respuestas, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }

    @PutMapping("/actualizar/otro/{codigo}")
    public ResponseEntity<?> actualizarDocumento(@Valid @RequestBody OtherDocument documento,
            @PathVariable Integer codigo, BindingResult result) {
        HashMap<String, Object> respuestas = new HashMap<>();
        ResponseEntity<?> respuesta = new ResponseEntity<>("No hay ninguna documento registrada", HttpStatus.NOT_FOUND);
        if (result.hasErrors()) {
            List<String> listError = new ArrayList<>();
            for (FieldError error : result.getFieldErrors()) {
                listError.add("Campo " + error.getField() + ":  " + error.getDefaultMessage());

            }
            respuestas.put("error", listError);
            return new ResponseEntity<Map<String, Object>>(respuestas, HttpStatus.BAD_REQUEST);
        }
        OtherDocument documentoDB = null;
        try {

            documentoDB = this.documentService.updateDocumentoOtro(documento, codigo);

            if (documentoDB != null) {
                respuesta = new ResponseEntity<OtherDocument>(documentoDB, HttpStatus.OK);
            } else {
                respuestas.put("mensaje", "El documento con codigo: " + codigo + " no existe en la base de datos");
                respuesta = new ResponseEntity<HashMap<String, Object>>(respuestas, HttpStatus.NOT_FOUND);
            }

        } catch (DataAccessException e) {
            respuestas.put("mensaje", "Error al realizar la consulta en la base de datos");
            respuestas.put("descripción del error", e.getMessage());
            respuesta = new ResponseEntity<HashMap<String, Object>>(respuestas, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }

    @GetMapping("/download/{documentName}")
    public ResponseEntity<?> downloadDocument(@PathVariable String documentName) {
        Map<String, Object> response = new HashMap<>();
        boolean flagCorrectPath= true;
        Path pathDocument = Paths.get("uploads").resolve(documentName).toAbsolutePath();
        Resource resource = null;
        try {
            resource = new UrlResource(pathDocument.toUri());
        } catch (Exception e) {
            flagCorrectPath =false;
        }
        if(flagCorrectPath== false || !resource.isReadable()){
            response.put("mensaje: ", "Error, no es posible acceder al recurso"+ documentName);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        } 
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ resource.getFilename()+"\"");
        return new ResponseEntity<Resource>(resource,header,HttpStatus.OK);
    }

    @GetMapping("/listarActas")
    public ResponseEntity<List<Acta>> listarActas(){
        List<Acta> allActas = this.documentService.getAllActas();
        if (allActas == null || allActas.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(allActas);
        }
    }

}
