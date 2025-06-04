package com.example.demo;

public class Usuario {
    private String usuario;
    private String email;
    private String nombre;
    private String apellidos;

    // Constructor vacío
    public Usuario() {}

    // Constructor con parámetros
    public Usuario(String usuario, String email, String nombre, String apellidos) {
        this.usuario = usuario;
        this.email = email;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    // Getters y Setters
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
}
