package co.edu.unicauca.asae.proyecto.maestriacomputacion.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Persona")
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona implements Serializable{
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_PERSONAS")
	@SequenceGenerator(name="S_PERSONAS", sequenceName="S_PERSONAS", allocationSize=1)
	private long id;
	
	@NotNull(message = "Please provider a tipoIdentificacion")
	String tipoIdentificacion;
	
	@NotNull(message = "Please provider a identificacion")
	@Column(unique = true)
	Long identificacion;
	
	@NotNull(message = "Please provider a nombres")
	@Size(min=2, max=30)
	String nombres;
	
	@NotNull(message = "Please provider a apellidos")
	@Size(min=2, max=30)
	String apellidos;
	
	
	String telefono;
	
	@Size(min=2, max=60)
	String direccion;
	
	@NotNull(message = "Please provider a correo")
	@Email(message = "Please provider a valid email address")
	@Column(unique = true)
	String correo;
	
	@NotNull(message = "Please provider a correoInstitucional")
	@Email(message = "Please provider a valid email address")
	@NotBlank(message = "The field cannot is empty string")
	String correoInstitucional;
	
	@NotNull(message = "Please provider a genero")
	String genero;

	@JsonIgnoreProperties("objPersona")
	@OneToOne(mappedBy = "objPersona", fetch = FetchType.LAZY)
	Usuario objUsuario;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}
	public Long getIdentificacion() {
		return identificacion;
	}
	public void setIdentificacion(Long identificacion) {
		this.identificacion = identificacion;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getCorreoInstitucional() {
		return correoInstitucional;
	}
	public void setCorreoInstitucional(String correoInstitucional) {
		this.correoInstitucional = correoInstitucional;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}

	public Persona(long id, String tipoIdentificacion, Long identificacion, String nombres,
				   String apellidos, String telefono,
				   String direccion, String correo,
				   String correoInstitucional,
				   String genero) {
		this.id = id;
		this.tipoIdentificacion = tipoIdentificacion;
		this.identificacion = identificacion;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.telefono = telefono;
		this.direccion = direccion;
		this.correo = correo;
		this.correoInstitucional = correoInstitucional;
		this.genero = genero;
	}

	public Persona() {
	}
}
