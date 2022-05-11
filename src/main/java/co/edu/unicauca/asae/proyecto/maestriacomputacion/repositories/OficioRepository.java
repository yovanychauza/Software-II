package co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Document;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Oficio;

import java.util.Date;
import java.util.List;

public interface OficioRepository extends JpaRepository<Oficio, Integer>{
    
    
    Page<Document> findAllByStatusTrue(Pageable pageable);
    public Oficio findDocumentByIdOficio(Integer codigo);
    public List<Document> findOficioByOfcDateBetween(Date dateStart, Date dateEnd);
    public Oficio findOficioByOfcNumber(Integer idOficio);
}

