package co.edu.unicauca.asae.proyecto.maestriacomputacion.response;

import java.util.List;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Asignatura;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsignaturaResponse {
	private List<Asignatura> asignaturas;
}
