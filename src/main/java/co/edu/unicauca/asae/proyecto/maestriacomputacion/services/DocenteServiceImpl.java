package co.edu.unicauca.asae.proyecto.maestriacomputacion.services;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Asignatura;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Docente;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories.DocenteRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.response.AsignaturaResponseRest;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.response.DocenteResponseRest;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocenteServiceImpl implements  IDocenteService{
    private static final Logger log = LoggerFactory.getLogger(AsignaturaServiceImpl.class);
    @Autowired
    private DocenteRepository docenteRepository;

    @Override
    public Docente save(Docente parDocente) {
        return this.docenteRepository.save(parDocente);
    }

    @Override
    public Docente findById(Long idDocente) {
        return docenteRepository.findById(idDocente).orElse(null);
    }
    

    @Override
    public ResponseEntity<DocenteResponseRest> crearDocentesPorDocumentos(MultipartFile multiPartFile){
        DocenteResponseRest response = new DocenteResponseRest();
        List<Docente> docentes = new ArrayList<>();
        try {
            Path tempDir = Files.createTempDirectory("");
            File tempFile = tempDir.resolve(multiPartFile.getOriginalFilename()).toFile();
            multiPartFile.transferTo(tempFile);
            Workbook workbook = WorkbookFactory.create(tempFile);
            Sheet sheet = workbook.getSheetAt(0);
            Stream<Row> rowsStream = StreamSupport.stream(sheet.spliterator(), false);
            List<List<String>> rows = rowsStream.map(r -> {
                Stream<Cell> cellStream = StreamSupport.stream(r.spliterator(), false);
                List<String> cellvals = cellStream.map(cell -> {
                    return validarValorCelda(cell);
                }).collect(Collectors.toList());
                return cellvals;
            }).collect(Collectors.toList());
            rows.remove(0);
            for (List<String> row : rows) {
                Docente docente = crearDocente(row);
                docentes.add(docente);
            }
            response.getDocenteResponse().setDocentes(docentes);
            response.setMetaData("Respuesta ok", "200", "Respuesta exitosa");
        } catch (IOException e1) {
            log.error(
                    "ERROR de E/S o en el metodo createTempDirectory() de la clase File: directorio de archivos temporales no existe");
            response.setMetaData("Respuesta nok", "-1",
                    "ERROR de E/S o en el metodo createTempDirectory() de la clase File: directorio de archivos temporales no existe");
            return new ResponseEntity<DocenteResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InvalidPathException e2) {
            log.error("ERROR en el metodo resolve() de la clase Path, si la cadena no se puede convertir en una ruta");
            response.setMetaData("Respuesta nok", "-1",
                    "ERROR en el metodo resolve() de la clase Path, si la cadena no se puede convertir en una ruta");
            return new ResponseEntity<DocenteResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalStateException e3) {
            log.error(
                    "ERROR en el metodo multiPartFile.transferTo() de la clase multiPartFile, el archivo ya se ha movido en el sistema de archivos");
            response.setMetaData("Respuesta nok", "-1",
                    "ERROR en el metodo multiPartFile.transferTo() de la clase multiPartFile, el archivo ya se ha movido en el sistema de archivos");
            return new ResponseEntity<DocenteResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<DocenteResponseRest>(response, HttpStatus.OK);
    }
    private String validarValorCelda(Cell cell) {
        String cellVal = "";
        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellVal = String.valueOf(cell.getDateCellValue());
                } else {
                    cellVal = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case STRING:
                cellVal = cell.getStringCellValue();
            default:
                break;
        }
        return cellVal;
    }
    private Docente crearDocente(List<String> lista) throws NumberFormatException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long a = (long)(Double.parseDouble(lista.get(0)));
        Docente docente= new Docente(a,lista.get(1), (long)(Double.parseDouble(lista.get(2))), lista.get(3),
                lista.get(4), lista.get(5), lista.get(6), lista.get(7), lista.get(8), lista.get(9),lista.get(10),lista.get(11),lista.get(12),lista.get(13),lista.get(14),
                lista.get(15),lista.get(16),lista.get(17),lista.get(18),lista.get(19),lista.get(20),
                lista.get(21));
        save(docente);
        return docente;
    }

    @Override
    public void delete(Long id) {
        docenteRepository.deleteById(id);
        
    }

    @Override
    @Transactional(readOnly = true)
    public List<Docente> findAll() {
        return (List<Docente>) docenteRepository.findAll();
    }

}
