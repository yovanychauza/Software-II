package co.edu.unicauca.asae.proyecto.maestriacomputacion.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estados")
public class Estados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "esta_descripcion")
    private String descripcion;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "estado", cascade = CascadeType.PERSIST)
    private List<EstudianteEstados> estudianteEstados = new ArrayList<>();

    public Estados(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estados() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @JsonManagedReference
    public List<EstudianteEstados> getEstudianteEstados() {
        return estudianteEstados;
    }

    public void setEstudianteEstados(List<EstudianteEstados> estudianteEstados) {
        this.estudianteEstados = estudianteEstados;
    }
}
