package co.edu.unicauca.asae.proyecto.maestriacomputacion.services;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Acta;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Document;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.OtherDocument;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Oficio;

import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDocumentService {

    /**
     *
     * @param documentType: Type of document required
     * @param id: Id of document required
     * @return a object type DocumentoOtro, Acta or Oficio
     */
    public Document getDocument(String documentType,Integer id);

    /**
     *
     * @param criterion: name or description
     * @return Document found or null
     */
    public List<Document> findDocumentByCriterion(String criterion);



    /**
     *
     * @param documentType
     * @param dateStart
     * @param dateEnd
     * @return Document found or null
     */
    public List<Document> findDocumentByDate(String documentType, String dateStart, String dateEnd) throws ParseException;

    //Metodo para eliminar logicamente un documento
    public Document eliminarDocumento(Integer codigo, String typeDocument);
    public Oficio saveOficio(Oficio oficio);
    public Acta saveActa(Acta acta);
    public OtherDocument saveDocumentoOtro(OtherDocument documentoOtro);
    public Page<Document> getDocuments(String documentType, Pageable pageable);
    public Oficio updateOficio(Oficio oficio, Integer codigo);
    public Acta updateActa(Acta acta, Integer codigo);
    public OtherDocument updateDocumentoOtro(OtherDocument documentoOtro, Integer codigo);

    /**
     * Eliminar acta de base de datos
     * @param idActa
     * @return Acta eliminada, null si no se encuentra el acta
     */
    public Acta deleteActa(Integer idActa);

    public List<Document> findDocumentByType(String type);

    public List<Acta> getAllActas();
}
