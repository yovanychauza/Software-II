package co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Estados;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IEstadosRepository extends CrudRepository<Estados, Long> {

    @Query(value = "select est from Estados est join est.estudianteEstados eest join eest.estudiante e where e.id=:idEstudiante")
    public Page<Estados> consultarEstadosPorEstudiante(@Param("idEstudiante") Long idEstudiante, Pageable pageable);

    @Query(value = "select est from Estados est join est.estudianteEstados eest join eest.estudiante e where e.id=:idEstudiante")
    public List<Estados> consultarEstadosPorEstudianteSinPaginacion(@Param("idEstudiante") Long idEstudiante);
}
