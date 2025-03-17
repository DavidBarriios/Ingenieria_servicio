package com.example.demo;

import com.example.demo.UsuarioDTO;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;

@Repository
public class UsuariosDAOTestdbg implements UsuariosDAOdbg { // Cambio de nombre aquí
    private ArrayList<UsuarioDTO> listaUsuarios = new ArrayList<>();

    public UsuariosDAOTestdbg() {
        // Agregamos usuarios de prueba
        listaUsuarios.add(new UsuarioDTO("admin", "admin123", "Administrador"));
        listaUsuarios.add(new UsuarioDTO("usuario1", "pass123", "Usuario Regular"));
    }

    @Override
    public UsuarioDTO validarUsuario(String nombreusuario, String password) {
        for (UsuarioDTO usuario : listaUsuarios) {
            if (usuario.getNombreusuario().equals(nombreusuario) && usuario.getPassword().equals(password)) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    public ArrayList<UsuarioDTO> obtenerUsuarios() {
        return listaUsuarios;
    }
}