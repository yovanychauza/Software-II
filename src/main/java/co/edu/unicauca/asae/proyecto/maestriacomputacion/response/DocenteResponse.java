package co.edu.unicauca.asae.proyecto.maestriacomputacion.response;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Asignatura;
import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Docente;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DocenteResponse {
    private List<Docente> docentes;
}
