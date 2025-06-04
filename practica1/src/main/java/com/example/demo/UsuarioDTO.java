package com.example.demo;

import java.io.Serializable;

public class UsuarioDTO implements Serializable {
    private String nombreusuario;
    private String email;
    private String nombre;
    private String apellidos;

    public UsuarioDTO() {}

    public UsuarioDTO(String nombreusuario, String email, String nombre, String apellidos) {
        this.nombreusuario = nombreusuario;
        this.email = email;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public String getNombreusuario() { return nombreusuario; }
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }

    public void setNombreusuario(String nombreusuario) { this.nombreusuario = nombreusuario; }
    public void setEmail(String email) { this.email = email; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
}