package com.example.nacho.lectorqr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Evento implements Serializable {
    private long id;
    private String nombre;
    private List<Alumno> listaAlumnos;

    public Evento() {
        listaAlumnos = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Alumno> getLista() {
        return listaAlumnos;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLista(List<Alumno> listaAlumnos) {
        this.listaAlumnos = listaAlumnos;
    }
}
