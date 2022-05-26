package co.edu.unicauca.asae.proyecto.maestriacomputacion.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "Docente")
@PrimaryKeyJoinColumn(name = "id_persona")
public class Docente extends Persona{

	@NotEmpty(message = "{doc.titulo.empty}")
	@Size(min=4, max=40, message = "{doc.titulo.size}")
	@Column(nullable=false, name = "doc_titulo")
	private String titulo;
	
	@Column(name = "doc_abreviatura")
	private String abreviatura;

	

	@NotEmpty(message = "{doc.universidad.empty}")
	@Size(min=4, max=30, message = "{doc.universidad.size}")
	@Column(nullable=false,name = "doc_universidad_titulo")
	private String universidadTitulo;

	@Column(name = "doc_categoria_mic")
	private String categoriaMic;

	@Size(min=7, max=100, message = "{doc.link.size}")
	@Column(nullable=true,name = "doc_lincvlac")
	private String lincVlac;

	@Size(min=2, max=30, message = "{doc.facultad.size}")
	@Column(name = "doc_id_facultad")
	private String idFacultad;
	
	@Size(min=4, max=30, message = "{doc.departamento.size}")
	@Column(nullable=true,name = "doc_departamento")
	private String departamento;

	@Size(min=4, max=40, message = "{doc.grupo.size}")
	@Column(nullable=true,name = "doc_grupo_inv")
	private String grupoInv;

	@Column(name = "doc_linea_inv")
	private String lineaInv;

	@Column(name = "doc_tipo_vinculacion")
	private String tipoVinculacion;

	@Column(name = "doc_escalafon")
	private String escalafon;

	@Size(min=4, max=100, message = "{doc.observacion.size}")
	@Column(nullable=true,name = "doc_observacion")
	private String observacion;

	public Docente() {
	}

	public Docente(long id, String tipoIdentificacion, Long identificacion, String nombres,
				   String apellidos, String telefono,
				   String direccion, String correo,
				   String correoInstitucional,
				   String genero, String titulo,String abreviatura, String universidadTitulo,
				   String categoriaMic, String lincVlac, String idFacultad,
				   String departamento, String grupoInv, String lineaInv,
				   String tipoVinculacion, String escalafon,
				   String observacion) {
		super(id, tipoIdentificacion, identificacion, nombres,
				apellidos, telefono,
				direccion,correo,
				correoInstitucional,
				genero);
		this.titulo = titulo;
		this.abreviatura =abreviatura;
		this.universidadTitulo = universidadTitulo;
		this.categoriaMic = categoriaMic;
		this.lincVlac = lincVlac;
		this.idFacultad = idFacultad;
		this.departamento = departamento;
		this.grupoInv = grupoInv;
		this.lineaInv = lineaInv;
		this.tipoVinculacion = tipoVinculacion;
		this.escalafon = escalafon;
		this.observacion = observacion;
	}
	public String getAbreviatura() {
		return this.abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}


	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getUniversidadTitulo() {
		return universidadTitulo;
	}
	public void setUniversidadTitulo(String universidadTitulo) {
		this.universidadTitulo = universidadTitulo;
	}
	public String getCategoriaMic() {
		return categoriaMic;
	}
	public void setCategoriaMic(String categoriaMic) {
		this.categoriaMic = categoriaMic;
	}
	public String getLincVlac() {
		return lincVlac;
	}
	public void setLincVlac(String lincVlac) {
		this.lincVlac = lincVlac;
	}
	public String getIdFacultad() {
		return idFacultad;
	}
	public void setIdFacultad(String idFacultad) {
		this.idFacultad = idFacultad;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getGrupoInv() {
		return grupoInv;
	}
	public void setGrupoInv(String grupoInv) {
		this.grupoInv = grupoInv;
	}
	public String getLineaInv() {
		return lineaInv;
	}
	public void setLineaInv(String lineaInv) {
		this.lineaInv = lineaInv;
	}
	public String getTipoVinculacion() {
		return tipoVinculacion;
	}
	public void setTipoVinculacion(String tipoVinculacion) {
		this.tipoVinculacion = tipoVinculacion;
	}
	public String getEscalafon() {
		return escalafon;
	}
	public void setEscalafon(String escalafon) {
		this.escalafon = escalafon;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

}
