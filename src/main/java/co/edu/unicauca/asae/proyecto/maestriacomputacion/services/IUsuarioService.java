package co.edu.unicauca.asae.proyecto.maestriacomputacion.services;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Usuario;

public interface IUsuarioService {

	public Usuario findByUsername(String username);
}
