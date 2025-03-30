package com.example.demo;

public class Userlogin {
    private String name;
    private String pass;
    private int es_admin;  // Cambio de es_user a es_admin
    
    // Constructor
    public Userlogin(String name, String pass, int es_admin) {
        this.name = name;
        this.pass = pass;
        this.es_admin = es_admin;
    }

    public Userlogin() {
        this.name = "";
        this.pass = "";
        this.es_admin = 0; // Asumimos que 0 es un valor predeterminado para usuarios no administradores
    }
    
    // Getters y setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getPass() {
        return pass;
    }
    
    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getEs_admin() {
        return es_admin; // Cambié es_user por es_admin
    }

    public void setEs_admin(int es_admin) {
        this.es_admin = es_admin;
    }
}
