package com.example.nacho.lectorqr;

import java.io.Serializable;

public class Alumno implements Serializable {
    private long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String numeroExpediente;
    private String titulacion;
    private long idEvento;

    public Alumno(long id, String nombre, String apellido, String dni, String numeroExpediente, long idEvento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.numeroExpediente = numeroExpediente;
        this.idEvento = idEvento;
    }

    public Alumno(String nombre, String apellido, String dni, String numeroExpediente, long idEvento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.numeroExpediente = numeroExpediente;
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

    public String getApellido() {
        return apellido;
    }

    public String getDni() {
        return dni;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public String getTitulacion(){ return titulacion; }

    public void setTitulacion(String titulacion){ this.titulacion = titulacion; }

    public long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(long idEvento) {
        this.idEvento = idEvento;
    }
}
