package com.example.nacho.lectorqr;

public class Alumno {
    private String nombre;
    private String apellido;
    private String dni;
    private String numeroExpediente;
    private String titulacion;

    public Alumno(){

    }

    public Alumno(String nombre, String apellido, String dni, String numeroExpediente) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.numeroExpediente = numeroExpediente;
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

}
