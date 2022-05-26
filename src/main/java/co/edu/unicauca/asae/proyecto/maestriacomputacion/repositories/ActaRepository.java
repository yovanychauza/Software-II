package co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories;
import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Acta;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Document;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Date;



public interface ActaRepository extends JpaRepository<Acta, Integer> {
    Page<Document> findAllByStatusTrue(Pageable pageable);
    public Acta findActaByIdActa(Integer id);
    public Acta findActaByNumber(Integer numActa);
    public List<Document> findActasByApprovalDateBetween(Date dateStart, Date dateEnd);

    /**
     * Paginado para listar las actas de una asignatura
     */
    @Query(
            value = "SELECT * " +
                    "FROM actas a " +
                    "WHERE a.id_acta IN " +
                    "(" +
                        "select aa.id_acta from asignaturas_actas aa where aa.codigo = :codigo" +
                    ")",
            nativeQuery = true
    )
    public Page<Acta> getActasByAsignatura(@Param("codigo") String codigo, Pageable pageable);
}
