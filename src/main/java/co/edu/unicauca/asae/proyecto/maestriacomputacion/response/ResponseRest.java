package co.edu.unicauca.asae.proyecto.maestriacomputacion.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;

@Getter
public class ResponseRest {
	
	private List<HashMap<String, String>> metaData = new ArrayList<HashMap<String,String>>();
	
	public void setMetaData(String tipo, String codigo, String comentario) {
		HashMap<String, String> map = new HashMap<String,String>();
		map.put("Tipo", tipo);
		map.put("Codigo", codigo);
		map.put("Comentario", comentario);
		this.metaData.add(map);
	}
}
