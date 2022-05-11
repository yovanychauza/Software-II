package co.edu.unicauca.asae.proyecto.maestriacomputacion.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "estudiantes")
@PrimaryKeyJoinColumn(name = "id_persona")
public class Estudiante extends Persona{

	@Column(name = "est_codigo")
	private String estCodigo;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "objEstudiante")
	@JsonBackReference
	private List<Prorroga> prorrogas;

	public Estudiante() {
		super();
		this.prorrogas = new ArrayList<Prorroga>();
	}
	public Estudiante(long id, String tipoIdentificacion, Long identificacion, String nombres, String apellidos, String telefono, String direccion, String correo, String correoInstitucional, String genero) {
		super(id, tipoIdentificacion, identificacion, nombres, apellidos, telefono, direccion, correo, correoInstitucional, genero);
	}

	public String getCodigo() {
		return estCodigo;
	}
	
	public void setCodigo(String estCodigo) {
		this.estCodigo = estCodigo;
	}

	public List<Prorroga> getProrrogas() {
		return this.prorrogas;
	}

	public void setProrrogas(List<Prorroga> prorrogas) {
		this.prorrogas = prorrogas;
	}

	public void addProrroga(Prorroga prorroga){
		prorrogas.add(prorroga);
		prorroga.setObjEstudiante(this);
	}

	public void removeProrroga(Prorroga prorroga){
		prorrogas.remove(prorroga);
		prorroga.setDocumento(null);
	}

}
