package com.example.demo;

import java.io.Serializable;

public class UsuarioDTO implements Serializable {
    private String nombreusuario;
    private String password;
    private String rol; // Puede ser "Administrador" o "Usuario Regular"

    // Constructor vacío (necesario para Spring y frameworks de serialización)
    public UsuarioDTO() {
    }

    // Constructor con parámetros
    public UsuarioDTO(String nombreusuario, String password, String rol) {
        this.nombreusuario = nombreusuario;
        this.password = password;
        this.rol = rol;
    }

    // Getters y Setters
    public String getNombreusuario() {
        return nombreusuario;
    }

    public void setNombreusuario(String nombreusuario) {
        this.nombreusuario = nombreusuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}