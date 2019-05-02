package com.example.cf17ericvisier.examenuf2android;

public class Incidencias {

    private String descripcion;
    private String aula;
    private String urlImagen;


    public Incidencias() {
    }

    public Incidencias(String descripcion, String aula, String urlImagen) {
        this.descripcion = descripcion;
        this.aula = aula;
        this.urlImagen = urlImagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
}
