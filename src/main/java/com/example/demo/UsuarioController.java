package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@SessionAttributes("user")
@Controller
public class UsuarioController {

    @Autowired
    private ServiciodbgInterface dao;
    
    // Método para mostrar la página de login
    @GetMapping("/login")
    public String mostrarLogin(HttpSession sesion, Model model) {
        Userlogin user = (Userlogin) sesion.getAttribute("user");

        // Si el usuario ya está logueado, redirige según el rol
        if (user != null) {
            if (user.getEs_admin() == 1) {
                model.addAttribute("usuarios", dao.getAllUsers()); // Cambié de lista_usuarios a usuarios
                return "admin"; // Página para el admin
            } else {
                model.addAttribute("usuario", user.getName());
                return "articulos"; // Página para usuarios regulares
            }
        }
        return "login"; // Si no está logueado, muestra la página de login
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

            // Redirige según si es administrador o usuario regular
            if (usuario.getEs_admin() == 1) {
                model.addAttribute("usuarios", dao.getAllUsers()); // Cambié de lista_usuarios a usuarios
                return "redirect:/datosusuario"; // Página de administración para el admin
            } else {
                model.addAttribute("usuario", name);
                return "redirect:/articulos"; // Página para el usuario regular
            }
        } else {
            // Si el usuario no existe o la contraseña es incorrecta
            model.addAttribute("exist", dao.existeusu(name) != null ? 2 : 1);  // 1: Usuario no encontrado, 2: Contraseña incorrecta
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
    public String registrarUsuario(@RequestParam String nombre, @RequestParam String password, @RequestParam int es_admin, Model model) {
        // Verifica si el nombre de usuario ya existe
        if (dao.existeusu(nombre) != null) {
            model.addAttribute("exist", true); // Si ya existe, muestra un mensaje
            return "registro"; // Vuelve a la página de registro
        } else {
            // Si no existe, crea el usuario
            dao.crearUsuario(nombre, password, es_admin);
            return "login"; // Redirige a la página de login después del registro
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

        // Si no hay sesión, redirige al login
        if (usuario == null) {
            return "redirect:/login"; 
        }

        // Si el usuario está logueado, muestra los artículos
        model.addAttribute("usuario", usuario.getName());
        return "articulos"; // Muestra la página de artículos
    }

    @GetMapping("/datosusuario")
    public String mostrarDatosUsuario(HttpSession sesion, Model model) {
        Userlogin usuario = (Userlogin) sesion.getAttribute("user");

        // Si no hay sesión o el usuario no es admin, redirige a login
        if (usuario == null || usuario.getEs_admin() != 1) {
            return "redirect:/login"; // Si no está logueado o no es admin, va al login
        }

        // Si es admin, muestra la página de datos de usuario (administrador)
        model.addAttribute("usuario", usuario.getName());
        model.addAttribute("usuarios", dao.getAllUsers()); // Cambié de lista_usuarios a usuarios
        return "datosusuario"; // Página de administración
    }
}
