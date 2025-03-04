package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.List;


@Controller
public class UsuarioController {
	
	//Muestra formulario de usuario    
	@GetMapping("/formulario")
	public String mostrarFormulario() {
		return "formulario";
	}
	
	@PostMapping("/datosusuario")
	public String metodo(HttpServletRequest req, HttpSession session) {
	    String usuario = req.getParameter("nombreusuario");
	    String email = req.getParameter("email");
	    String nombre = req.getParameter("nombre");
	    String apellidos = req.getParameter("apellidos");

	    Usuario user = new Usuario(usuario, email, nombre, apellidos);
	    session.setAttribute("usuario", user);  // Guarda en la sesión

	    return "redirect:/datosusuario"; // Redirige para evitar duplicados en formularios
	}
	
	// Muestra los datos del usuario almacenados en la sesión
    @GetMapping("/datosusuario")
    public String mostrarDatosUsuario(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            model.addAttribute("usuario", usuario);
        } else {
            model.addAttribute("mensaje", "No hay datos de usuario en la sesión.");
        }

        return "datosusuario";
    }
	
	

}
