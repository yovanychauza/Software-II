package co.edu.unicauca.asae.proyecto.maestriacomputacion.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Acta;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Asignatura;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories.ActaRepository;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories.IAsignaturaRepository;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.response.ActaResponseRest;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.response.AsignaturaResponseRest;

@Service
public class AsignaturaServiceImpl implements IAsignaturaService {
	private static final Logger log = LoggerFactory.getLogger(AsignaturaServiceImpl.class);

	private static final String INACTIVE_STATUS = "INACTIVA";

	@Autowired
	private IAsignaturaRepository asignaturaRepository;

	@Autowired
	private ActaRepository actaRepository;

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<AsignaturaResponseRest> findAll() {
		log.info("Inicio metodo Find All");
		AsignaturaResponseRest response = new AsignaturaResponseRest();
		try {
			List<Asignatura> listaAsignaturas = asignaturaRepository.findAll();
			if(listaAsignaturas.isEmpty()){
				log.error("Error al listar las asignaturas");
				response.setMetaData("Respuestas nok", "-1","No existe Asignaturas registradas");
				return new ResponseEntity<AsignaturaResponseRest>(response, HttpStatus.NO_CONTENT);
			}
			response.getAsignaturaReponse().setAsignaturas(listaAsignaturas);
			response.setMetaData("Respuesta OK","200","Respuesta exitosa");

		}catch(Exception e) {
			log.error("Error al consultar las asignaturas", e.getMessage());
			e.getStackTrace();
			return new ResponseEntity<AsignaturaResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return  new ResponseEntity<AsignaturaResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional(readOnly = true)
	public Asignatura findById(String codigo) {
		Asignatura objAsignatura = null;
		Optional<Asignatura> optAsignatura = this.asignaturaRepository.findById(codigo);
		if (optAsignatura.isPresent()) {
			objAsignatura = optAsignatura.get();
		}
		return objAsignatura;
	}

	@Override
	@Transactional
	public Asignatura save(Asignatura asignatura) {
		return this.asignaturaRepository.save(asignatura);
	}

	@Override
	@Transactional
	public Asignatura update(Asignatura asignatura, String codigo) {
		Asignatura asignaturaDB = this.asignaturaRepository.findById(codigo).orElse(null);
		Asignatura resultado = null;
		if (asignaturaDB != null) {
			if (asignatura.getNombre() != null)
				asignaturaDB.setNombre(asignatura.getNombre());

			if (asignatura.getEstado() != null)
				asignaturaDB.setEstado(asignatura.getEstado());

			if (asignatura.getFechaAprobacion() != null)
				asignaturaDB.setFechaAprobacion(asignatura.getFechaAprobacion());

			if (asignatura.getAreaFormacion() != null)
				asignaturaDB.setAreaFormacion(asignatura.getAreaFormacion());

			if (asignatura.getLineaInvestigacion() != null)
				asignaturaDB.setLineaInvestigacion(asignatura.getLineaInvestigacion());

			if (asignatura.getTipo() != null)
				asignaturaDB.setTipo(asignatura.getTipo());

			if (asignatura.getNumeroCreditos() != null)
				asignaturaDB.setNumeroCreditos(asignatura.getNumeroCreditos());

			if (asignatura.getObjetivo() != null)
				asignaturaDB.setObjetivo(asignatura.getObjetivo());

			if (asignatura.getContenido() != null)
				asignaturaDB.setContenido(asignatura.getContenido());

			if (asignatura.getHorasPresenciales() != null)
				asignaturaDB.setHorasPresenciales(asignatura.getHorasPresenciales());

			if (asignatura.getHorasNoPresenciales() != null)
				asignaturaDB.setHorasNoPresenciales(asignatura.getHorasNoPresenciales());
			
			resultado = this.asignaturaRepository.save(asignaturaDB);
		}

		return resultado;
	}

	@Override
	@Transactional
	public Optional<Asignatura> deactivateAsignatura(String codigo) {
		log.info("Desactivando asignatura con codigo {}", codigo);
		Optional<Asignatura> optAsignatura = this.asignaturaRepository.findActiveByCodigo(codigo);

		// Si se encuentra la asignatura, se le cambia el estado
		optAsignatura.ifPresent(asignatura -> {
			this.asignaturaRepository.updateEstado(asignatura.getCodigo(), INACTIVE_STATUS);
			log.info("Desactivada asignatura con codigo {}, nuevo estado: {}", codigo, INACTIVE_STATUS);
		});
		return optAsignatura;
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<List<Asignatura>> findByCodigoOrNombreOrEstado(String busqueda){
		List<Asignatura> listResult = this.asignaturaRepository.findByCodigoIgnoreCaseContainingOrNombreIgnoreCaseContainingOrEstadoIgnoreCaseContaining(busqueda, busqueda, busqueda);
		return ResponseEntity.ok(listResult);
	}


	@Override
	@Transactional(readOnly = true)
	public List<Asignatura> findByNombre(String nombre) {
		return this.asignaturaRepository.findByNombreAndEstadoIgnoreCase(nombre, "Activa");
	}

	@Override
	@Transactional(readOnly = true)
	public List<Asignatura> findByEstado(String Estado) {
		return this.asignaturaRepository.findByEstadoIgnoreCase(Estado);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Asignatura> findByFechaAprobacion(Date fechaAprobacion) {
		return this.asignaturaRepository.findByFechaAprobacion(fechaAprobacion);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Asignatura> findByAreaInvestigacion(String areaInvestigacion) {
		return this.asignaturaRepository.findByAreaFormacionAndEstadoIgnoreCase(areaInvestigacion, "activa");
	}

	@Override
	@Transactional(readOnly = true)
	public List<Asignatura> findByLineaInvestigacion(String lineaInvestigacion) {
		return this.asignaturaRepository.findByLineaInvestigacionAndEstadoIgnoreCase(lineaInvestigacion, "activa");
	}

	@Override
	@Transactional(readOnly = true)
	public List<Asignatura> findByTipo(String tipo) {
		return this.asignaturaRepository.findByTipoAndEstadoIgnoreCase(tipo, "activa");
	}

	@Override
	@Transactional(readOnly = true)
	public List<Asignatura> findByNumeroCreditos(Integer numeroCreditos) {
		return this.asignaturaRepository.findByNumeroCreditosAndEstadoIgnoreCase(numeroCreditos, "activa");
	}

	@Override
	@Transactional(readOnly = true)
	public List<Asignatura> findByHorasPresenciales(Integer horasPresenciales) {
		return this.asignaturaRepository.findByHorasPresencialesAndEstadoIgnoreCase(horasPresenciales, "activa");
	}

	@Override
	@Transactional(readOnly = true)
	public List<Asignatura> findByHorasNoPresenciales(Integer horasNoPresenciales) {
		return this.asignaturaRepository.findByHorasNoPresencialesAndEstadoIgnoreCase(horasNoPresenciales, "activa");
	}

	@Override
	public ResponseEntity<AsignaturaResponseRest> guardarAsignatura(Asignatura asignatura) {
		log.info("Inicio metodo guardarAsignagtura()");
		AsignaturaResponseRest response = new AsignaturaResponseRest();
		List<Asignatura> asignaturas = new ArrayList<Asignatura>();
		try {
			Asignatura asignaturaGuardada = this.asignaturaRepository.save(asignatura);
			if (asignaturaGuardada != null) {
				asignaturas.add(asignaturaGuardada);
				response.getAsignaturaReponse().setAsignaturas(asignaturas);
				response.setMetaData("Respuesta ok", "200", "Respuesta exitosa");
			} else {
				log.error("Error al guardar asignatura");
				response.setMetaData("Respuesta nok", "-1", "Asignatura no guardada");
				return new ResponseEntity<AsignaturaResponseRest>(response, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			log.error("Error al guardar la asignatura", e.getMessage());
			e.getStackTrace();
			response.setMetaData("Respuesta nok", "-1", "Error al guardar la Asignatura");
			return new ResponseEntity<AsignaturaResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<AsignaturaResponseRest>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<AsignaturaResponseRest> cargarAsignaturasExcel(MultipartFile multiPartFile) {
		log.info("Inicio metodo cargar excel");
		AsignaturaResponseRest response = new AsignaturaResponseRest();
		List<Asignatura> asignaturas = new ArrayList<>();
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
				Asignatura asignatura = crearAsignatura(row);
				asignaturas.add(asignatura);
			}
			response.getAsignaturaReponse().setAsignaturas(asignaturas);
			response.setMetaData("Respuesta ok", "200", "Respuesta exitosa");
		} catch (IOException e1) {
			log.error(
					"ERROR de E/S o en el metodo createTempDirectory() de la clase File: directorio de archivos temporales no existe");
			response.setMetaData("Respuesta nok", "-1",
					"ERROR de E/S o en el metodo createTempDirectory() de la clase File: directorio de archivos temporales no existe");
			return new ResponseEntity<AsignaturaResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (InvalidPathException e2) {
			log.error("ERROR en el metodo resolve() de la clase Path, si la cadena no se puede convertir en una ruta");
			response.setMetaData("Respuesta nok", "-1",
					"ERROR en el metodo resolve() de la clase Path, si la cadena no se puede convertir en una ruta");
			return new ResponseEntity<AsignaturaResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IllegalStateException e3) {
			log.error(
					"ERROR en el metodo multiPartFile.transferTo() de la clase multiPartFile, el archivo ya se ha movido en el sistema de archivos");
			response.setMetaData("Respuesta nok", "-1",
					"ERROR en el metodo multiPartFile.transferTo() de la clase multiPartFile, el archivo ya se ha movido en el sistema de archivos");
			return new ResponseEntity<AsignaturaResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ParseException e4) {
			log.error(
					"ERROR en el metodo parse() se la clase SimpleDateFormat(String cadena):no se puede analizar el comienzo de la cadena especificada");
			response.setMetaData("Respuesta nok", "-1",
					"ERROR en el metodo parse() se la clase SimpleDateFormat(String cadena):no se puede analizar el comienzo de la cadena especificada");
			return new ResponseEntity<AsignaturaResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<AsignaturaResponseRest>(response, HttpStatus.OK);
	}

	private Asignatura crearAsignatura(List<String> lista) throws NumberFormatException, ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Asignatura asignatura = Asignatura.builder().codigo(lista.get(0)).nombre(lista.get(1)).estado(lista.get(2))
				.fechaAprobacion(format.parse(lista.get(3))).areaFormacion(lista.get(4))
				.lineaInvestigacion(lista.get(5)).tipo(lista.get(6))
				.numeroCreditos((int) Double.parseDouble(lista.get(7))).objetivo(lista.get(8)).contenido(lista.get(9))
				.horasPresenciales((int) Double.parseDouble(lista.get(10)))
				.horasNoPresenciales((int) Double.parseDouble(lista.get(11))).build();
		guardarAsignatura(asignatura);
		return asignatura;
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

	@Override
	public ResponseEntity<AsignaturaResponseRest> asociarActaAsignatura(Integer numeroActa, String codigo) {
		log.info("Inicio metodo asociar asignatura con acta");
		AsignaturaResponseRest response = new AsignaturaResponseRest();
		List <Asignatura> listAsignaturas = new ArrayList<Asignatura>();
		try{
			Optional <Asignatura> optAsignatura = this.asignaturaRepository.findById(codigo);
			Asignatura objAsignatura = optAsignatura.orElse(null);
			Acta acta = (Acta) this.actaRepository.findActaByNumber(numeroActa);
			if(objAsignatura!=null || acta!= null){
				objAsignatura.agregarActa(acta);
				listAsignaturas.add(objAsignatura);
				this.asignaturaRepository.save(objAsignatura);
				response.getAsignaturaReponse().setAsignaturas(listAsignaturas);
				response.setMetaData("Respuesta ok","200","Respuesta exitosa");
			}else{
				log.error("Error al consultar asignatura o acta");
				response.setMetaData("Respuesta nok","-1","Error al consultar asignatura o acta");
				return new ResponseEntity<AsignaturaResponseRest>(response, HttpStatus.NOT_FOUND);
			}	
		}catch(Exception e){
			log.error("Error al asociar el acta con la asignatura, "+e.getMessage());
			response.setMetaData("Respuesta nok", "-1", "Error al asociar el acta con la asignatura "+e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<AsignaturaResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<AsignaturaResponseRest>(response, HttpStatus.OK);
	}

	@Override
	public Asignatura actualizarActasAsignatura(String codigoAsignatura, List<Integer> actasIds) {
		System.out.println("Actualizar actasd e asignatura " + codigoAsignatura);
		Asignatura foundAsignatura = this.asignaturaRepository.findById(codigoAsignatura).orElse(null);

		if (foundAsignatura != null && actasIds != null) {
			if (foundAsignatura.getActas() != null) {
				foundAsignatura.getActas().forEach(acta -> acta.getAsignaturas().remove(foundAsignatura));
				this.actaRepository.saveAll(foundAsignatura.getActas());
				foundAsignatura.getActas().clear();
			}
			actasIds.forEach(id -> {
				Optional<Acta> optFoundActa = this.actaRepository.findById(id);
				optFoundActa.ifPresent(acta -> {
					foundAsignatura.getActas().add(acta);
				});
			});
			this.asignaturaRepository.save(foundAsignatura);
		}
		return foundAsignatura;
	}


	@Override
    public ResponseEntity<Page<Acta>> findByAsignatura(String codigo, Integer page) {
		Pageable pageable = PageRequest.of(page,2);
		log.info("Inicio método findByAsignatura");
		try{
			Optional <Asignatura> optAsignatura = this.asignaturaRepository.findById(codigo);
			Asignatura objAsignatura = optAsignatura.orElse(null);
			if(objAsignatura!=null){
				Page<Acta> listActas = actaRepository.getActasByAsignatura(codigo, pageable);

				if(listActas.isEmpty()){
					log.error("Error, La asignatura no tiene actas asociadas");
					return new ResponseEntity<>(listActas, HttpStatus.NO_CONTENT);
				}
				return new ResponseEntity<>(listActas, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e){
			log.error("Error al cargar las actas de la asignatura: ", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

	@Override
	@Transactional(readOnly = true)
	public List<Asignatura> findAllAsignatura() {
		return this.asignaturaRepository.findAll();
	}


}
