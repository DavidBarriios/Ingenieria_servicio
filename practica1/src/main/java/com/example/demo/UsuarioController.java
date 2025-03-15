package com.example.demo;

import com.example.demo.UsuarioDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UsuarioController {

    // Muestra el formulario de usuario
    @GetMapping("/formulario")
    public String mostrarFormulario() {
        return "formulario"; // Devuelve la vista "formulario.html" en /templates/
    }

    // Procesa el formulario y almacena datos en sesión + cookie
    @PostMapping("/datosusuario")
    public String guardarDatosUsuario(
            @RequestParam("nombreusuario") String usuario,
            @RequestParam("email") String email,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellidos") String apellidos,
            HttpSession session, HttpServletResponse response) {

        // Crear objeto UsuarioDTO
        UsuarioDTO user = new UsuarioDTO(usuario, email, nombre, apellidos);
        session.setAttribute("usuario", user); // Guardar en sesión

        // Crear una cookie con el email del usuario
        Cookie cookie = new Cookie("emailUsuario", email);
        cookie.setMaxAge(60 * 60 * 24 * 7); // Expira en 7 días
        cookie.setPath("/"); // Disponible en todo el dominio
        response.addCookie(cookie);

        return "redirect:/datosusuario"; // Redirige para evitar reenvíos del formulario
    }

    // Muestra los datos del usuario almacenados en la sesión y lee la cookie
    @GetMapping("/datosusuario")
    public String mostrarDatosUsuario(HttpSession session, HttpServletRequest request, Model model) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

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
                    model.addAttribute("cookieEmail", cookie.getValue());
                }
            }
        }

        return "datosusuario"; // Carga la vista "datosusuario.html"
    }
     // 📌 NUEVA FUNCIÓN: Muestra el formulario de login
        @GetMapping("/login")
        public String mostrarLogin() {
            return "login";
        }

        // 📌 NUEVA FUNCIÓN: Procesa el formulario de login
        @PostMapping("/login")
        public ModelAndView procesarLogin(
                @RequestParam("email") String email,
                @RequestParam("password") String password,
                HttpSession session) {

            // Simulación de autenticación (puedes cambiar por una consulta a la BD)
            if (email.equals("admin@example.com") && password.equals("1234")) {
                UsuarioDTO usuario = new UsuarioDTO("Admin", email, "Administrador", "Sistema");
                session.setAttribute("usuario", usuario);
                return new ModelAndView("redirect:/articulos");
            } else {
                ModelAndView modelAndView = new ModelAndView("login");
                modelAndView.addObject("error", "Credenciales incorrectas");
                return modelAndView;
            }
        }

        // 📌 NUEVA FUNCIÓN: Cierra sesión
        @GetMapping("/logout")
        public String logout(HttpSession session) {
            session.invalidate();
            return "redirect:/login";
        }

        // 📌 NUEVA FUNCIÓN: Vista de artículos (tipo W3Schools)
        @GetMapping("/articulos")
        public String mostrarArticulos() {
            return "articulos";
        }
    }