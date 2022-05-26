

package co.edu.unicauca.asae.proyecto.maestriacomputacion.entities;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonBackReference;


import lombok.Data;

@Entity(name = "oficios")
@Table(name = "oficios")
@Data
public class Oficio implements Document {

    @Id
    //@NotNull(message = "{oficio.idOficio.notNull}")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name="id_oficio")
    private Integer idOficio;
    
    @NotNull(message = "{oficio.ofcNumero.notNull}")
    @Column(name = "ofc_numero",unique=true, nullable = false)
    @PositiveOrZero(message = "{oficio.ofcNumero.positive}")
    private Integer ofcNumber;

    
    @NotNull(message = "{oficio.ofcFecha.notNull}")
	@DateTimeFormat(iso = ISO.DATE)
	@PastOrPresent(message = "{oficio.ofcFecha.past}")
    @Column(name = "ofc_fecha")
    private Date ofcDate;
    
    @NotNull(message = "{oficio.ofcNombre.notNull}")
    @Size(min = 2, max = 45, message="{oficio.ofcNombre.size}")
    @Column(name = "ofc_nombre")
    private String name;
    
    @NotNull(message = "{oficio.ofcOficio.notNull}")
    @Size(min = 2, max = 45, message="{oficio.ofcOficio.size}")
    private String ofcOficio;
    
    @NotNull(message = "{oficio.ofcLinkOficio.notNulll}")
    @Size(min = 2, message="{oficio.ofcLinkOficio.size}")
    @Column(name = "ofc_link")
    private String ofcLink;
    
    @NotNull(message = "{oficio.ofcLinkSoporte.notNull}")
    @Size(min = 2, message="{oficio.ofcLinkSoporte.size}")
    @Column(name = "ofc_link_soporte")
    private String supportLink;
    
    @NotNull(message = "{oficio.ofcestado.notNull}")
    @Column(name = "ofc_estado")
    private boolean status;
    
    @NotNull(message = "{oficio.ofcVersion.notNull}")
    @Size(min = 2, max = 45, message="{oficio.ofcVersion.size}")
    @Column(name = "ofc_version")
    private String version;

    private String archivo;

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