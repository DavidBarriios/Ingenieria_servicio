package com.example.demo;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.HashMap;

@Controller
@SessionAttributes("usuariosRegistrados") // Guarda la lista de usuarios en sesión
public class UsuarioController {

    private final Map<String, UsuarioDTO> usuariosRegistrados = new HashMap<>(); // Almacena usuarios

    // Inicializa la lista de usuarios en la sesión si no existe
    @ModelAttribute("usuariosRegistrados")
    public Map<String, UsuarioDTO> getUsuariosRegistrados() {
        return usuariosRegistrados;
    }
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    //Mostrar formulario de login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    // Procesar login
    @PostMapping("/login")
    public String procesarLogin(@RequestParam("nombreusuario") String nombreusuario,
                                @RequestParam("password") String password,
                                HttpSession session,
                                Model model) {
        
        Usuario usuario = usuarioRepository.findByNombreusuario(nombreusuario);

        if (usuario == null) {
            model.addAttribute("error", "Usuario no encontrado");
            return "login";
        }

        if (!usuario.getPassword().equals(password)) {
            model.addAttribute("error", "Contraseña incorrecta");
            return "login";
        }

        session.setAttribute("usuario", usuario);

        return "ADMIN".equals(usuario.getRol()) ? "redirect:/datosusuario" : "redirect:/articulos";
    }


    // Procesar registro
    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam("usuario") String usuario,
                                   @RequestParam("password") String password,
                                   @RequestParam("rol") String rol,
                                   Model model) {
        
        if (usuarioRepository.findByNombreusuario(usuario) != null) {
            model.addAttribute("error", "El usuario ya está registrado.");
            return "registro";
        }

        usuarioRepository.save(new Usuario(usuario, password, rol));
        
        return "redirect:/login";
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