package co.edu.unicauca.asae.proyecto.maestriacomputacion.entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Prorroga")
public class Prorroga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pro_identificador")
    private Long idProrroga;

    @NotNull(message = "{prorroga.fecha.notNull}")
    @Column(name = "pro_fecha")
    private Date fecha;

    @Column(name = "pro_num_resolucion", unique = true)
    @NotNull(message = "{prorroga.numResolucion.notNull}")
    @PositiveOrZero(message = "{prorroga.numResolucion.positive}")
    @Size(min = 2, message = "{prorroga.numResolucion.size}")
    private String numResolucion;

    @OneToOne
    @JsonIgnoreProperties(value = {"objProrroga"}, allowSetters = true)
    @NotNull(message = "{prorroga.documento.notNull}")
    @JoinColumn(name = "pro_doc_id", unique = true)
	private OtherDocument objDocumento;

    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante objEstudiante;

    public Long getIdProrroga() {
        return this.idProrroga;
    }

    public void setIdProrroga(Long idProrroga) {
        this.idProrroga = idProrroga;
    }

    public Date getFecha() {
        return this.fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNumResolucion() {
        return this.numResolucion;
    }

    public void setNumResolucion(String numResolucion) {
        this.numResolucion = numResolucion;
    }

    public OtherDocument getDocumento() {
        return this.objDocumento;
    }

    public void setDocumento(OtherDocument objDocumento) {
        this.objDocumento = objDocumento;
    }

    public Estudiante getObjEstudiante() {
        return this.objEstudiante;
    }

    public void setObjEstudiante(Estudiante objEstudiante) {
        this.objEstudiante = objEstudiante;
    }
}
