package com.example.nacho.lectorqr;

public class Alumno {
    private String nombre;
    private String apellido;
    private String dni;
    private long numeroExpediente;

    public Alumno(String nombre, String apellido, String dni, long numeroExpediente) {
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

    public long getNumeroExpediente() {
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

    public void setNumeroExpediente(long numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }
}
