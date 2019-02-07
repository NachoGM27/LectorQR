package com.example.nacho.lectorqr;

import java.util.List;

public class Evento {
    private long id;
    private String nombre;
    private List<Alumno> lista;

    public Evento() {
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Alumno> getLista() {
        return lista;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLista(List<Alumno> lista) {
        this.lista = lista;
    }
}
