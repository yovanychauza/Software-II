package co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories;
import java.util.List;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Document;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.OtherDocument;

public interface OtherDocumentRepository extends JpaRepository<OtherDocument, Integer>{
    
    Page<Document> findAllByStatusTrue(Pageable pageable);
    public List<OtherDocument> findByName(String nombre);
    public Document findDocumentByDocId(Integer id);
    public List<Document> findAllByNameIsContainingAndStatusTrueOrDescriptionContainingAndStatusTrue(String criterion, String criterionTwo);
    public List<Document> findAllByDocumentTypeIgnoreCaseAndStatusTrue(String type);
}
