package com.example.nacho.lectorqr;

import java.io.Serializable;

public class Alumno implements Serializable {
    private long id;
    private String nombre;
    private String campoExtra1;
    private String campoExtra2;
    private String campoExtra3;
    private long idEvento;

    public Alumno(long id, String nombre, String campoExtra1, String campoExtra2, String campoExtra3, long idEvento) {
        this.id = id;
        this.nombre = nombre;
        this.campoExtra1 = campoExtra1;
        this.campoExtra2 = campoExtra2;
        this.campoExtra3 = campoExtra3;
        this.idEvento = idEvento;
    }

    public Alumno(String nombre, String campoExtra1, String campoExtra2, String campoExtra3, long idEvento) {
        this.nombre = nombre;
        this.campoExtra1 = campoExtra1;
        this.campoExtra2 = campoExtra2;
        this.campoExtra3 = campoExtra3;
        this.idEvento = idEvento;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
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

    public long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(long idEvento) {
        this.idEvento = idEvento;
    }
}
