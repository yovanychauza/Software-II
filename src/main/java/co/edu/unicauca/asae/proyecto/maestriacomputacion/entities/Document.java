package co.edu.unicauca.asae.proyecto.maestriacomputacion.entities;

import javax.persistence.MappedSuperclass;


public interface Document {
    public void setEstado(boolean estado);
    public boolean getEstado();
    public void setVersion(String version);
    public String getVersion();
    public void setArchivo(String archivo);
    public String getArchivo();
}
