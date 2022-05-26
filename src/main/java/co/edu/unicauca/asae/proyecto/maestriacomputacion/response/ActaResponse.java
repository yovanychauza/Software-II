package co.edu.unicauca.asae.proyecto.maestriacomputacion.response;

import java.util.List;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Acta;
import lombok.Data;

@Data
public class ActaResponse {
    private List <Acta> listActas;
}
