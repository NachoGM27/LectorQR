package com.example.nacho.lectorqr;

import java.io.Serializable;

public class Evento implements Serializable {

    private long id;
    private String nombre;

    public Evento(String nombre) {
        this.nombre = nombre;
    }

    public Evento(long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
