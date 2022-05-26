package co.edu.unicauca.asae.proyecto.maestriacomputacion.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "asignaturas")
@Data
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Asignatura {

	@Id
	private String codigo;

	@NotNull(message = "{asignatura.nombre.notNull}")
	@Size(min = 2, max = 45, message="{asignatura.nombre.size}")
	@Column(unique = true)
	private String nombre;

	@NotNull(message = "{asignatura.estado.notNull}")
	private String estado;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE)
	@PastOrPresent(message = "{asignatura.fechaAprobacion.past}")
	@Column(name = "fecha_aprobacion")
	private Date fechaAprobacion;

	@NotNull(message = "{asignatura.areaFormacion.notNull}")
	@Size(min = 2, max = 45, message="{asignatura.areaFormacion.size}")
	private String areaFormacion;
	
	@NotNull(message = "{asignatura.lineaInvestigacion.notNull}")
	@Size(min = 2, max = 45, message="{asignatura.lineaInvestigacion.size}")
	private String lineaInvestigacion;
	
	@NotNull(message = "{asignatura.tipo.notNull}")
	@Size(min = 2, max = 45, message="{asignatura.tipo.size}")
	private String tipo;
	
	@NotNull(message = "{asignatura.numeroCreditos.notNull}")
	@PositiveOrZero(message = "{asignatura.numeroCreditos.positive}")
	private Integer numeroCreditos;
	
	@NotNull(message = "{asignatura.objetivo.notNull}")
	@Size(min = 2, max = 45, message="{asignatura.objetivo.size}")
	private String objetivo;
	
	@NotNull(message = "{asignatura.contenido.notNull}")
	@Size(min = 2, max = 45, message="{asignatura.contenido.size}")
	private String contenido;
	
	@NotNull(message = "{asignatura.horasPresenciales.notNull}")
	@PositiveOrZero(message = "{asignatura.horasPresenciales.positive}")
	private Integer horasPresenciales;
	
	@NotNull(message = "{asignatura.horasNoPresenciales.notNull}")
	@PositiveOrZero(message = "{asignatura.horasNoPresenciales.positive}")
	private Integer horasNoPresenciales;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "asignaturas_actas", joinColumns = @JoinColumn(name = "codigo"),
	 			inverseJoinColumns = @JoinColumn(name = "id_acta"), 
				uniqueConstraints = { @UniqueConstraint(columnNames = { "codigo", "id_acta" }) })
	private List<Acta> actas;
	
	public boolean agregarActa(Acta acta) {
		return this.actas.add(acta);
	}

	@Override
	public String toString() {
		return "Asignatura [actas=" + actas + ", areaFormacion=" + areaFormacion + ", codigo=" + codigo + ", contenido="
				+ contenido + ", estado=" + estado + ", fechaAprobacion=" + fechaAprobacion + ", horasNoPresenciales="
				+ horasNoPresenciales + ", horasPresenciales=" + horasPresenciales + ", lineaInvestigacion="
				+ lineaInvestigacion + ", nombre=" + nombre + ", numeroCreditos=" + numeroCreditos + ", objetivo="
				+ objetivo + ", tipo=" + tipo + "]";
	}

}
