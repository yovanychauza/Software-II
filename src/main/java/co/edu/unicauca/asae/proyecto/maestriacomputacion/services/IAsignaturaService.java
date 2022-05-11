package co.edu.unicauca.asae.proyecto.maestriacomputacion.services;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Acta;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Asignatura;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.response.ActaResponseRest;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.response.AsignaturaResponseRest;

public interface IAsignaturaService {
	
	public ResponseEntity<AsignaturaResponseRest> findAll();
	public List<Asignatura> findAllAsignatura();
	public Asignatura findById(String codigo);
	public Asignatura save(Asignatura asignatura);
	public Asignatura update(Asignatura asignatura, String codigo);
	public Optional<Asignatura> deactivateAsignatura(String codigo);
	
	// Metodos de busqueda creados segun un criterio propio
	public List<Asignatura> findByNombre(String nombre);
	public List<Asignatura> findByEstado(String Estado);
	public List<Asignatura> findByFechaAprobacion(Date fechaAprobacion);
	public List<Asignatura> findByAreaInvestigacion(String areaInvestigacion);
	public List<Asignatura> findByLineaInvestigacion(String lineaInvestigacion);
	public List<Asignatura> findByTipo(String tipo);
	public List<Asignatura> findByNumeroCreditos(Integer numeroCreditos);
	public List<Asignatura> findByHorasPresenciales(Integer horasPresenciales);
	public List<Asignatura> findByHorasNoPresenciales(Integer horasNoPresenciales);
	public ResponseEntity<List<Asignatura>> findByCodigoOrNombreOrEstado(String busqueda);

	public ResponseEntity<AsignaturaResponseRest>cargarAsignaturasExcel(MultipartFile multiPartFile);
	public ResponseEntity<AsignaturaResponseRest> guardarAsignatura(Asignatura asignatura);
	
	public ResponseEntity<AsignaturaResponseRest> asociarActaAsignatura(Integer numeroActa, String codigo);
	public Asignatura actualizarActasAsignatura(String codigoAsignatura, List<Integer> actasIds);

	public ResponseEntity<Page<Acta>> findByAsignatura(String codigo, Integer page);
}
