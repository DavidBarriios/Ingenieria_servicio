package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuarioController {

    @Autowired
    private ServiciodbgInterface dao;

    // Método para mostrar la página de login
    @GetMapping("/login")
    public String mostrarLogin(HttpSession sesion, Model model) {
        Userlogin user = (Userlogin) sesion.getAttribute("user");

        if (user == null) {
            return "login"; // Si el usuario no está logueado, muestra la página de login
        } else {
            if (user.getEs_admin() == 1) {
                // Si el usuario es administrador, muestra la lista de usuarios
                model.addAttribute("lista_usuarios", dao.getAllUsers());
                return "admin"; // Página para el admin
            } else {
                model.addAttribute("usuario", user.getName());
                return "articulos"; // Página para usuarios regulares
            }
        }
    }

    // Método para procesar el login
    @PostMapping("/login")
    public String procesarLogin(HttpServletRequest req, Model model) {
        HttpSession sess = req.getSession();
        String pass = req.getParameter("password");
        String name = req.getParameter("nombreusuario");

        // Verifica si el usuario y la contraseña son correctos
        Userlogin usuario = dao.checkuser(name, pass);

        if (usuario != null) {
            // Si el usuario existe, lo guarda en la sesión
            sess.setAttribute("user", usuario);
            sess.setAttribute("name", name);

            if (usuario.getEs_admin() == 1) {
                return "redirect:/datosusuario"; // Redirige a datosusuario si es admin
            } else {
                return "redirect:/articulos"; // Redirige a articulos si es usuario normal
            }
        } else {
            // Si el usuario no existe o la contraseña es incorrecta
            model.addAttribute("exist", dao.existeusu(name) != null ? 2 : 1);
            return "login"; // Vuelve a mostrar la página de login
        }
    }

    // Método para mostrar la página de registro
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro"; // Página de registro
    }

    // Método para registrar un nuevo usuario
    @PostMapping("/registro")
    public String registrarUsuario(HttpServletRequest req, Model model) {
        String pass = req.getParameter("password");
        String name = req.getParameter("nombre");
        int esAdmin = Integer.parseInt(req.getParameter("es_admin")); // Convertir a entero

        // Verifica si el nombre de usuario ya existe
        if (dao.existeusu(name) != null) {
            model.addAttribute("exist", true); // Si ya existe, muestra un mensaje
            return "registro"; // Vuelve a la página de registro
        } else {
            // Si no existe, crea el usuario
            dao.crearUsuario(name, pass, esAdmin);
            return "redirect:/login"; // Redirige correctamente a la página de login
        }
    }
    

    // Método para cerrar sesión
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalida la sesión
        return "redirect:/login"; // Redirige a la página de login
    }
    
    // Método para mostrar la página de artículos (para usuarios logueados)
    @GetMapping("/articulos")
    public String mostrarArticulos(HttpSession sesion, Model model) {
        Userlogin usuario = (Userlogin) sesion.getAttribute("user");

        if (usuario == null) {
            return "redirect:/login"; // Si no hay sesión, redirige al login
        }

        model.addAttribute("usuario", usuario.getName());
        return "articulos"; // Muestra la página de artículos
    }
    
    @GetMapping("/datosusuario")
    public String mostrarDatosUsuario(HttpSession sesion, Model model) {
        Userlogin usuario = (Userlogin) sesion.getAttribute("user");

        if (usuario == null || usuario.getEs_admin() != 1) {
            return "redirect:/login"; // Si no está logueado o no es admin, lo redirige al login
        }

        model.addAttribute("usuarios", dao.getAllUsers()); // Agregar lista de usuarios
        return "datosusuario"; // Asegúrate de que exista una vista "datosusuario.html"
    }
    
}