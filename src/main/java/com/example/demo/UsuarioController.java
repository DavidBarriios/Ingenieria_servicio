package com.example.demo;

import com.example.demo.Usuario;
import com.example.demo.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Mostrar formulario de login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    // Procesar login
    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("nombreusuario") String nombreusuario,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        System.out.println("🔍 Intentando iniciar sesión con usuario: " + nombreusuario);

        // Buscar usuario en la base de datos
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreusuario(nombreusuario);

        if (usuarioOpt.isEmpty()) {
            model.addAttribute("error", "Usuario no encontrado");
            return "login";
        }

        Usuario usuario = usuarioOpt.get();

        // Validar contraseña
        if (!usuario.getPassword().equals(password)) {
            model.addAttribute("error", "Contraseña incorrecta");
            return "login";
        }

        // Guardar usuario en sesión
        session.setAttribute("usuario", usuario);
        System.out.println("Usuario guardado en sesión: " + session.getAttribute("usuario"));

        // Redirección según el rol
        if ("Administrador".equalsIgnoreCase(usuario.getRol().trim())) {
            return "redirect:/datosusuario";
        } else {
            return "redirect:/articulos";
        }
    }

    // Mostrar formulario de registro
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    // Procesar registro
    @PostMapping("/registro")
    public String registrarUsuario(
            @RequestParam("usuario") String nombreusuario,
            @RequestParam("password") String password,
            @RequestParam("rol") String rol,
            Model model) {

        // Verificar si el usuario ya existe
        if (usuarioRepository.findByNombreusuario(nombreusuario).isPresent()) {
            model.addAttribute("error", "El usuario ya está registrado.");
            return "registro";
        }

        // Guardar usuario en base de datos
        Usuario nuevoUsuario = new Usuario(nombreusuario, password, rol);
        usuarioRepository.save(nuevoUsuario);

        return "redirect:/login";
    }

    // Cerrar sesión
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // Vista de artículos (para usuarios normales)
    @GetMapping("/articulos")
    public String mostrarArticulos(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        return "articulos";
    }

    // Vista para el Administrador (Lista de Usuarios)
    @GetMapping("/datosusuario")
    public String mostrarUsuarios(Model model, HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");

        if (usuarioLogueado == null || !"Administrador".equalsIgnoreCase(usuarioLogueado.getRol().trim())) {
            return "redirect:/articulos";
        }

        // Obtener lista de usuarios desde la base de datos
        model.addAttribute("usuarios", usuarioRepository.findAll());

        return "datosusuario";
    }
}