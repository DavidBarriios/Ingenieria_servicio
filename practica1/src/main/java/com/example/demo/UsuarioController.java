package com.example.demo;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsuarioController {
    
    // Muestra el formulario de usuario
    @GetMapping("/formulario")
    public String mostrarFormulario() {
        return "formulario";
    }

    @PostMapping("/datosusuario")
    public String metodo(HttpServletRequest req, HttpSession session, HttpServletResponse response) {
        String usuario = req.getParameter("nombreusuario");
        String email = req.getParameter("email");   //ffsfsff
        String nombre = req.getParameter("nombre");
        String apellidos = req.getParameter("apellidos");

        Usuario user = new Usuario(usuario, email, nombre, apellidos);
        session.setAttribute("usuario", user); // Guarda en la sesión

        // Crear una cookie con el email del usuario
        Cookie cookie = new Cookie("emailUsuario", email);
        cookie.setMaxAge(60 * 60 * 24 * 7); // Expira en 7 días
        cookie.setPath("/"); // Disponible en todo el dominio
        response.addCookie(cookie); // Agregar la cookie a la respuesta

        return "redirect:/datosusuario"; // Redirige para evitar reenvíos del formulario
    }

    // Muestra los datos del usuario almacenados en la sesión y lee la cookie
    @GetMapping("/datosusuario")
    public String mostrarDatosUsuario(HttpSession session, HttpServletRequest request, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            model.addAttribute("usuario", usuario);
        } else {
            model.addAttribute("mensaje", "No hay datos de usuario en la sesión.");
        }

        // Leer cookies enviadas por el navegador
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("emailUsuario".equals(cookie.getName())) {
                    System.out.println("Cookie encontrada: " + cookie.getValue());
                    model.addAttribute("cookieEmail", cookie.getValue()); // Mostrar en la vista opcionalmente
                }
            }
        }

        return "datosusuario";
    }
}