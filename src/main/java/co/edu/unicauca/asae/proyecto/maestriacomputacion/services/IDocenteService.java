package co.edu.unicauca.asae.proyecto.maestriacomputacion.services;

import java.util.List;
import java.util.Optional;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Docente;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.response.AsignaturaResponseRest;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.response.DocenteResponseRest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IDocenteService {
    public Docente save(Docente parEstudiante);
    public Docente findById(Long idDocente);
    public ResponseEntity<DocenteResponseRest> crearDocentesPorDocumentos(MultipartFile multiPartFile);
    public void delete(Long id);
    public List<Docente> findAll();
}
