package co.edu.unicauca.asae.proyecto.maestriacomputacion.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "reingresos")
public class Reingreso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rein_id", nullable = false)
	private int id;

	@NotNull(message = "{reingreso.FechaIngreso.notNull}")
	@Column(name = "rein_fecha_ingreso")
	@PastOrPresent(message = "{oficio.ofcFecha.past}")
	@Temporal(TemporalType.DATE)
	private Date FechaIngreso;

	@Column(name = "rein_num_resolucion")
	@NotNull(message = "{reingreso.NumResolucion.notNull}")
	private String NumResolucion;

	@Column(name = "rein_sem_financiero")
	@NotNull(message = "{reingreso.SemestreFinanciero.notNull}")
	@Positive(message = "{reingreso.SemestreFinanciero.positive}")
	private Integer SemestreFinanciero;

	@Column(name = "rein_sem_academico")
	@NotNull(message = "{reingreso.SemestreAcademico.notNull}")
	@Positive(message = "{reingreso.SemestreAcademico.positive}")
	private Integer SemestreAcademico;

	@ManyToOne
	@JoinColumn(name = "rein_est_codigo")
	@NotNull(message = "{reingreso.Estudiante.notNull}")
	private Estudiante objEstudiante;

	@OneToOne
	@JsonIgnoreProperties("objReingreso")
	@NotNull(message = "{reingreso.DocumentoOtro.notNull}")
	@JoinColumn(name = "rein_doc_id", unique = true)
	private OtherDocument objDocumentoOtro;

	/* Getters and Setters */
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getFechaIngreso() {
		return this.FechaIngreso;
	}

	public void setFechaIngreso(Date FechaIngreso) {
		this.FechaIngreso = FechaIngreso;
	}

	public String getNumResolucion() {
		return this.NumResolucion;
	}

	public void setNumResolucion(String NumResolucion) {
		this.NumResolucion = NumResolucion;
	}

	public int getSemestreFinanciero() {
		return this.SemestreFinanciero;
	}

	public void setSemestreFinanciero(int SemestreFinanciero) {
		this.SemestreFinanciero = SemestreFinanciero;
	}

	public int getSemestreAcademico() {
		return this.SemestreAcademico;
	}

	public void setSemestreAcademico(int SemestreAcademico) {
		this.SemestreAcademico = SemestreAcademico;
	}

	public Estudiante getObjEstudiante() {
		return this.objEstudiante;
	}

	public void setObjEstudiante(Estudiante objEstudiante) {
		this.objEstudiante = objEstudiante;
	}

	public OtherDocument getObjDocumentoOtro() {
		return this.objDocumentoOtro;
	}

	public void setObjDocumentoOtro(OtherDocument objDocumentoOtro) {
		this.objDocumentoOtro = objDocumentoOtro;
	}

}
