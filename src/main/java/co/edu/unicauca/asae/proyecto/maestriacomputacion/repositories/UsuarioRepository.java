package co.edu.unicauca.asae.proyecto.maestriacomputacion.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Usuario;



public interface UsuarioRepository extends CrudRepository<Usuario, Long>{
	
	public Usuario findByUsername(String username);
	
	@Query("select u from Usuario u where u.username=?1")
	public Usuario findByUsername2(String username);

}