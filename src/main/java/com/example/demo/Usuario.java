package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombreusuario;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String rol;

    public Usuario() {}

    public Usuario(String nombreusuario, String password, String rol) {
        this.nombreusuario = nombreusuario;
        this.password = password;
        this.rol = rol;
    }

    public String getNombreusuario() { return nombreusuario; }
    public void setNombreusuario(String nombreusuario) { this.nombreusuario = nombreusuario; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}