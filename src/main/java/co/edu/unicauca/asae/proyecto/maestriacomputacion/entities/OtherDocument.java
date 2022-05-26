package co.edu.unicauca.asae.proyecto.maestriacomputacion.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity(name = "otros_documentos")
@Table(name = "otros_documentos")
@Data
public class OtherDocument implements Document {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// @NotNull(message = "{docotro.id.notNull}")
	@JoinColumn(name = "id_doc")
	private Integer docId;

	@Size(min = 5, max = 20, message = "{docotro.nombre.size}")
	@NotEmpty(message = "{docotro.nombre.empty}")
	@Column(name = "doc_nombre")
	private String name;

	@NotEmpty(message = "{docotro.version.empty}")
	@Column(name = "doc_version")
	private String version;

	@Size(min = 5, max = 30, message = "{docotro.descripcion.size}")
	@NotEmpty(message = "{docotro.descripcion.empty}")
	@Column(name = "doc_descripcion")
	private String description;

	@NotEmpty(message = "{docotro.ruta.empty}")
	@Column(name = "doc_ruta")
	private String path;

	@NotNull(message = "{docotro.estado.notNull}")
	@Column(name = "doc_estado")
	private Boolean status;

	@NotEmpty(message = "{docotro.link.empty}")
	@Column(name = "doc_link")
	private String documentLink;

	@NotEmpty(message = "{docotro.linksoporte.empty}")
	@Column(name = "doc_link_soporte")
	private String supportLink;

	@NotEmpty(message = "{docotro.tipodoc.empty}")
	@Column(name = "doc_tipo")
	private String documentType;

	private String archivo;

	@JsonIgnoreProperties("objDocumentoOtro")
	@OneToOne(mappedBy = "objDocumentoOtro", fetch = FetchType.LAZY)
	Reingreso objReingreso;

	@JsonIgnoreProperties(value = { "objDocumento" })
	@JsonBackReference
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "objDocumento", fetch = FetchType.LAZY)
	Prorroga objProrroga;

	public void setObjProrroga(Prorroga objProrroga) {
		this.objProrroga = objProrroga;
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	@Override
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	@Override
	public String getArchivo() {
		return archivo;
	}

	@Override
	public void setEstado(boolean estado) {
		this.status = estado;
	}

	@Override
	public boolean getEstado() {
		return status;
	}

	@Override
	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String getVersion() {
		return this.version;
	}

}
