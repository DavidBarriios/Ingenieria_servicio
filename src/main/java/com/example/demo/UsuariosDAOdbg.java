package com.example.demo;

import com.example.demo.UsuarioDTO;
import java.util.ArrayList;

public interface UsuariosDAOdbg { // Cambio de nombre aquí
    UsuarioDTO validarUsuario(String nombreusuario, String password);
    ArrayList<UsuarioDTO> obtenerUsuarios();
}