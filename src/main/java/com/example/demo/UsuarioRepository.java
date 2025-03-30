
package com.example.demo;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepository implements ServiciodbgInterface {

    private JdbcTemplate jdbctemplate;

    @Autowired
    public void setDataSource(DataSource datasource) { 
        this.jdbctemplate = new JdbcTemplate(datasource);
    }

    private final RowMapper<Userlogin> mapper = (rs, numRow) -> {
        Userlogin usuario = new Userlogin();
        usuario.setNombre(rs.getString("nombre_usuario")); // Asigna el nombre de usuario
        usuario.setPass(rs.getString("contraseña")); // Asigna la contraseña
        usuario.setEs_admin(rs.getInt("es_admin")); // Asigna el rol (es_admin)
        return usuario;
    };

    // Método para registrar un nuevo usuario
    @Override
    public void crearUsuario(String nombre, String contraseña, int esAdmin) {
        String sql = "INSERT INTO usuarios (nombre_usuario, contraseña, es_admin) VALUES (?, ?, ?)";
        jdbctemplate.update(sql, nombre, contraseña, esAdmin);
    }

    // Obtener todos los usuarios
    @Override
    public List<Userlogin> getAllUsers() {
        String sql = "SELECT * FROM usuarios";
        return this.jdbctemplate.query(sql, mapper);
    }

    // Verificar si un usuario existe con nombre y contraseña
    @Override
    public Userlogin checkuser(String nombre, String contraseña) {
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ? AND contraseña = ?";
        List<Userlogin> usuarios = this.jdbctemplate.query(sql, mapper, nombre, contraseña);
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }

    // Verificar si un usuario existe solo con el nombre
    @Override
    public Userlogin existeusu(String nombre) {
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ?";
        List<Userlogin> usuarios = this.jdbctemplate.query(sql, mapper, nombre);
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }
}