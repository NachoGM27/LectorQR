package com.example.nacho.lectorqr;

import java.io.Serializable;

public class Evento implements Serializable {

    private long id;
    private String nombre;
    private String campoExtra1;
    private String campoExtra2;
    private String campoExtra3;

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

    public String getCampoExtra1() {
        return campoExtra1;
    }

    public void setCampoExtra1(String campoExtra1) {
        this.campoExtra1 = campoExtra1;
    }

    public String getCampoExtra2() {
        return campoExtra2;
    }

    public void setCampoExtra2(String campoExtra2) {
        this.campoExtra2 = campoExtra2;
    }

    public String getCampoExtra3() {
        return campoExtra3;
    }

    public void setCampoExtra3(String campoExtra3) {
        this.campoExtra3 = campoExtra3;
    }
}
