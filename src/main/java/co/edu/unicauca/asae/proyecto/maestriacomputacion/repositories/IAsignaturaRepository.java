package co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Asignatura;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IAsignaturaRepository extends JpaRepository<Asignatura, String> {
	
	public List<Asignatura> findByNombreAndEstadoIgnoreCase(String nombre, String estado);
	public List<Asignatura> findByEstadoIgnoreCase(String estado);
	public List<Asignatura> findByCodigoIgnoreCaseContainingOrNombreIgnoreCaseContainingOrEstadoIgnoreCaseContaining(String codigo, String nombre, String estado);
	public List<Asignatura> findByFechaAprobacion(Date fechaAprobacion);
	public List<Asignatura> findByAreaFormacionAndEstadoIgnoreCase(String areaInvestigacion, String estado);
	public List<Asignatura> findByLineaInvestigacionAndEstadoIgnoreCase(String lineaInvestigacion, String estado);
	public List<Asignatura> findByTipoAndEstadoIgnoreCase(String tipo, String estado);
	public List<Asignatura> findByNumeroCreditosAndEstadoIgnoreCase(Integer numeroCreditos, String estado);
	public List<Asignatura> findByHorasPresencialesAndEstadoIgnoreCase(Integer horasPresenciales, String estado);
	public List<Asignatura> findByHorasNoPresencialesAndEstadoIgnoreCase(Integer horasNoPresenciales, String estado);

	@Query (
		value = "SELECT * " +
				"FROM asignaturas a " +
				"WHERE a.estado != 'INACTIVA' AND a.codigo = :codigo",
		nativeQuery = true
	)
	public Optional<Asignatura> findActiveByCodigo(@Param("codigo") String codigo);

	@Modifying
	@Query (
			value = "UPDATE asignaturas " +
					"SET estado = :estado " +
					"WHERE codigo = :codigo",
			nativeQuery = true
	)
	public void updateEstado(@Param("codigo") String codigo, @Param("estado") String estado);
	
}
