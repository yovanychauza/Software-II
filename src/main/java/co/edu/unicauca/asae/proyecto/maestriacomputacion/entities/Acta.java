package co.edu.unicauca.asae.proyecto.maestriacomputacion.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="actas")
@Table(name = "actas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Acta implements Document {

	
	//private Integer numeroActa;
	//private String linkActa;
	//@Id
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@NotNull(message = "{acta.id.notNull}")
	private Integer idActa;

	@Column(name="numero_acta",unique=true, nullable = false)
	@NotNull(message = "{acta.numero.notNull}")
	@PositiveOrZero(message = "{acta.numero.positive}")

	private Integer number;
	
	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "fecha_aprobacion")
	@PastOrPresent(message = "{acta.fecha.past}")
	private Date approvalDate;

	@Size(min=5,max=20, message = "{acta.nombre.size}")
	@NotEmpty(message = "{acta.nombre.empty}")
	@Column(name = "act_nombre")
	private String name;

	@NotNull(message = "{acta.estado.notNull}")
	@Column(name = "act_estado")
	private Boolean status;

	@NotEmpty(message = "{acta.linkActa.empty}")
	@Column(name = "act_link")
	private String actLink;

	@NotEmpty(message = "{acta.linkSoporte.empty}")
	@Column(name = "act_link_soporte")
	private String supportLink;

	@NotEmpty(message = "{acta.actVersion.empty}")
	@Column(name = "act_version")
	private String version;

	private String archivo;
	
	@JsonBackReference

	

	@ManyToMany(mappedBy = "actas",fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE })
	private List<Asignatura> asignaturas;

	
	public boolean agregarAsignatura(Asignatura asignatura) {
		return this.asignaturas.add(asignatura);
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
		this.version =version;
	}

	@Override
	public String getVersion() {
		return this.version;
	}

}
