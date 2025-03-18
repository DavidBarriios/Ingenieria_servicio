package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.HashMap;

@Controller
@SessionAttributes("usuariosRegistrados") // Guarda la lista de usuarios en sesión
public class UsuarioControllerdbg {

    private final Map<String, UsuarioDTO> usuariosRegistrados = new HashMap<>(); // Almacena usuarios

    // Inicializa la lista de usuarios en la sesión si no existe
    @ModelAttribute("usuariosRegistrados")
    public Map<String, UsuarioDTO> getUsuariosRegistrados() {
        return usuariosRegistrados;
    }

    //Mostrar formulario de login
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

        // Obtener la lista de usuarios desde la sesión
        Map<String, UsuarioDTO> usuariosRegistrados = 
            (Map<String, UsuarioDTO>) session.getAttribute("usuariosRegistrados");

        if (usuariosRegistrados == null) {
            model.addAttribute("error", "No hay usuarios registrados.");
            return "login";
        }

        // Buscar el usuario en la lista
        UsuarioDTO usuarioDTO = usuariosRegistrados.get(nombreusuario);

        if (usuarioDTO == null) {
            model.addAttribute("error", "Usuario no encontrado");
            return "login";
        }

        // Validar contraseña
        if (!usuarioDTO.getPassword().equals(password)) {
            model.addAttribute("error", "Contraseña incorrecta");
            return "login";
        }

        // 📌 Depuración: Verificar el rol
        System.out.println("Usuario encontrado: " + usuarioDTO.getNombreusuario() + ", Rol: '" + usuarioDTO.getRol() + "'");

        // Guardar usuario en sesión
        session.setAttribute("usuario", usuarioDTO);
        System.out.println("Usuario guardado en sesión: " + session.getAttribute("usuario"));

        // Verificar si la redirección es correcta
        if ("Administrador".equalsIgnoreCase(usuarioDTO.getRol().trim())) { 
            System.out.println("Redirigiendo a /datosusuario");
            return "redirect:/datosusuario";
        } else {
            System.out.println("Redirigiendo a /articulos");
            return "redirect:/articulos";
        }
    }

    // 📌 Procesar registro
    @PostMapping("/registro")
    public ModelAndView registrarUsuario(
            @RequestParam("usuario") String usuario,
            @RequestParam("password") String password,
            @RequestParam("rol") String rol,
            HttpSession session) {

        ModelAndView modelAndView = new ModelAndView();

        // Recuperar o inicializar la lista de usuarios en sesión
        Map<String, UsuarioDTO> usuariosRegistrados = 
            (Map<String, UsuarioDTO>) session.getAttribute("usuariosRegistrados");

        if (usuariosRegistrados == null) {
            usuariosRegistrados = new HashMap<>();
            session.setAttribute("usuariosRegistrados", usuariosRegistrados);
        }

        if (usuariosRegistrados.containsKey(usuario)) {
            modelAndView.setViewName("registro");
            modelAndView.addObject("error", "El usuario ya está registrado.");
        } else {
            UsuarioDTO nuevoUsuario = new UsuarioDTO(usuario, password, rol);
            usuariosRegistrados.put(usuario, nuevoUsuario);

            System.out.println("Usuarios registrados después del registro:");
            usuariosRegistrados.forEach((k, v) -> System.out.println("Usuario: " + k + ", Rol: " + v.getRol()));

            modelAndView.setViewName("redirect:/login");
        }

        return modelAndView;
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
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
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        //Si no hay usuario en sesión, redirigir al login
        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        return "articulos";
    }

    // Vista para el Administrador (Lista de Usuarios)
    @GetMapping("/datosusuario")
    public String mostrarUsuarios(Model model, HttpSession session) {
        UsuarioDTO usuarioLogueado = (UsuarioDTO) session.getAttribute("usuario");

        //Si no hay usuario o no es admin, redirigir a artículos
        if (usuarioLogueado == null || !"Administrador".equalsIgnoreCase(usuarioLogueado.getRol().trim())) {
            return "redirect:/articulos";
        }

        // Obtener lista de usuarios desde la sesión
        Map<String, UsuarioDTO> usuariosRegistrados = 
            (Map<String, UsuarioDTO>) session.getAttribute("usuariosRegistrados");

        if (usuariosRegistrados == null) {
            usuariosRegistrados = new HashMap<>();
            session.setAttribute("usuariosRegistrados", usuariosRegistrados);
        }

        // Verificación en consola
        System.out.println("📌 Usuarios almacenados en sesión:");
        usuariosRegistrados.forEach((k, v) -> System.out.println("Usuario: " + k + ", Rol: " + v.getRol()));

        model.addAttribute("usuarios", usuariosRegistrados.values());
        return "datosusuario";
    }
}