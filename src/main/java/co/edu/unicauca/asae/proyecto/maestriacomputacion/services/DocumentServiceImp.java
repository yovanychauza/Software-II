package co.edu.unicauca.asae.proyecto.maestriacomputacion.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.*;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.exception.DeleteActaException;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories.IAsignaturaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories.ActaRepository;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories.OtherDocumentRepository;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories.OficioRepository;

@Service
public class DocumentServiceImp implements IDocumentService{

    @Autowired
    private ActaRepository actaRepository;
    @Autowired
    private OficioRepository oficioRepository;
    @Autowired
    private OtherDocumentRepository otherDocumentRepository;
    @Autowired
    private IAsignaturaRepository asignaturaRepository;

    public Document getDocument(String documentType, Integer numDoc){
        if(documentType.equalsIgnoreCase("ACTA"))
            return this.actaRepository.findActaByIdActa(numDoc);
        else if (documentType.equalsIgnoreCase("OFICIO"))
            return this.oficioRepository.findOficioByOfcNumber(numDoc);
        else
            return this.otherDocumentRepository.findDocumentByDocId(numDoc);
    }

    /**
     * @param criterion: name or description
     * @return Document found or null
     */
    @Override
    public List<Document> findDocumentByCriterion(String criterion) {
        List<Document> documentList= this.otherDocumentRepository.findAllByNameIsContainingAndStatusTrueOrDescriptionContainingAndStatusTrue(criterion, criterion);

        for (Document doc:documentList) {
            System.out.println(doc.getEstado());

        }
        return documentList;
    }

    /**
     * @param documentType
     * @param dateStart
     * @param dateEnd
     * @return Document found or null
     */
    @Override
    public List<Document> findDocumentByDate(String documentType, String dateStart, String dateEnd) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("YYYY-MM-dd");
        if(documentType.equalsIgnoreCase("ACTA"))
            return  this.actaRepository.findActasByApprovalDateBetween(sdf.parse(dateStart), sdf2.parse(dateEnd));
        else if (documentType.equalsIgnoreCase("OFICIO"))
            return this.oficioRepository.findOficioByOfcDateBetween(sdf.parse(dateStart), sdf2.parse(dateEnd));
        else
            return null;
    }

    @Override
    public Document eliminarDocumento(Integer codigo, String typeDocument){
        Document objDocRecuperado = null;
        Document doc_eliminado = null;

        objDocRecuperado = this.getDocument(typeDocument, codigo);

        if(objDocRecuperado != null){
            if(typeDocument.equalsIgnoreCase("ACTA")){
                doc_eliminado = deleteActa(codigo);
            }else if(typeDocument.equalsIgnoreCase("OFICIO")){
                System.out.println("Entramos");
                doc_eliminado = this.deleteOficio(codigo);
            }else{
                doc_eliminado = this.deleteOtro(codigo);
            }
        }
        return doc_eliminado;
    }

    @Override
	@Transactional
	public Oficio saveOficio(Oficio oficio) {
        return this.oficioRepository.save(oficio);
	}
    @Override
    @Transactional
    public Acta saveActa(Acta acta) {
        return this.actaRepository.save(acta);
    }

    @Override
    @Transactional
    public OtherDocument saveDocumentoOtro(OtherDocument documentoOtro) {
        return this.otherDocumentRepository.save(documentoOtro);
    }

    //Oscar
    @Override
    public Oficio updateOficio(Oficio oficio, Integer codigo) {
        Oficio oficioDB = this.oficioRepository.findOficioByOfcNumber(codigo);
        Oficio resultado = null;
        if (oficioDB != null) {
            resultado = this.oficioRepository.save(oficio);
        }
        return resultado;
    }

    @Override
    public Acta updateActa(Acta acta, Integer codigo) {
        Acta actaDB = this.actaRepository.findById(codigo).orElse(null);
        Acta resultado = null;
        if (actaDB != null) {
            resultado = this.actaRepository.save(acta);
        }
        return resultado;
    }

    @Override
    public OtherDocument updateDocumentoOtro(OtherDocument documentoOtro, Integer codigo) {
        OtherDocument documentoDB = this.otherDocumentRepository.findById(codigo).orElse(null);
        OtherDocument resultado = null;
        if (documentoDB != null) {
            resultado = this.otherDocumentRepository.save(documentoOtro);
        }
        return resultado;
    }

    @Override
    @Transactional
    public Acta deleteActa(Integer idActa) throws DeleteActaException {
        Optional<Acta> optFoundActa = this.actaRepository.findById(idActa);

        // Elimino el acta si existe y no tiene asignaturas asociadas
        optFoundActa.ifPresent(acta -> {
            if (acta.getAsignaturas() == null || acta.getAsignaturas().isEmpty()) {
                this.actaRepository.delete(acta);
            } else {
                throw new DeleteActaException("No se puede eliminar el acta [" + acta.getName() + "] porque tiene asignaturas asociadas");
            }
        });
        return optFoundActa.orElse(null);
    }

    @Override
    public List<Document> findDocumentByType(String type) {
        return this.otherDocumentRepository.findAllByDocumentTypeIgnoreCaseAndStatusTrue(type);
    }

    @Override
    public List<Acta> getAllActas() {
        return this.actaRepository.findAll();
    }



    @Override
    @Transactional
    public Page<Document> getDocuments(String documentType, Pageable pageable) {
        Page<Document> documents =null;
        if(documentType.equalsIgnoreCase("ACTA"))
            documents = this.actaRepository.findAllByStatusTrue(pageable);

        else if (documentType.equalsIgnoreCase("OFICIO"))
            documents = this.oficioRepository.findAllByStatusTrue(pageable);
        else
            documents= this.otherDocumentRepository.findAllByStatusTrue(pageable);
        return documents;
    }

    public Oficio deleteOficio(Integer codigo) throws DeleteActaException {
        Oficio oficioDB = null;
        oficioDB = this.oficioRepository.findOficioByOfcNumber(codigo);
        try {
            if (oficioDB != null) {
                this.oficioRepository.deleteById(oficioDB.getIdOficio());
            }
        } catch (Exception e) {
            throw new DeleteActaException("No se puede eliminar el documento [" + oficioDB.getName()+ "] porque tiene algunos registros asociadas");
        }
        return oficioDB;
    }

    public OtherDocument deleteOtro(Integer codigo) throws DeleteActaException {
        OtherDocument documentoDB = this.otherDocumentRepository.findById(codigo).orElse(null);
        try {
            if (documentoDB != null) {
                this.otherDocumentRepository.deleteById(codigo);
            }
        } catch (Exception e) {
            throw new DeleteActaException("No se puede eliminar el documento [" + documentoDB.getName() + "] porque tiene algunos registros asociadas");
        }
        return documentoDB;
    }

}
