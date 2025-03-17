package com.example.demo;

import com.example.demo.UsuarioDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.HashMap;

@Controller
@SessionAttributes("usuariosRegistrados") // ✅ Almacena la lista de usuarios en sesión
public class UsuarioController {

    private Map<String, UsuarioDTO> usuariosRegistrados = new HashMap<>(); // Almacena usuarios

    // ✅ Método para inicializar usuarios en sesión si aún no existen
    @ModelAttribute("usuariosRegistrados")
    public Map<String, UsuarioDTO> getUsuariosRegistrados() {
        return usuariosRegistrados;
    }

    // 📌 Mostrar formulario de login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    // 📌 Procesar login
    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("nombreusuario") String nombreusuario,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        System.out.println("Intentando iniciar sesión con usuario: " + nombreusuario);

        UsuarioDTO usuarioDTO = usuariosRegistrados.get(nombreusuario);

        if (usuarioDTO == null) {
            System.out.println("Error: Usuario no encontrado.");
            model.addAttribute("error", "Usuario no encontrado");
            return "login";
        }

        if (!usuarioDTO.getPassword().equals(password)) {
            System.out.println("Error: Contraseña incorrecta.");
            model.addAttribute("error", "Contraseña incorrecta");
            return "login";
        }

        System.out.println("Inicio de sesión exitoso. Redirigiendo a /articulos");
        session.setAttribute("usuario", usuarioDTO);
        return "redirect:/articulos";
    }

    // 📌 Procesar registro
    @PostMapping("/registro")
    public ModelAndView registrarUsuario(
            @RequestParam("usuario") String usuario,
            @RequestParam("password") String password,
            @RequestParam("rol") String rol,
            @ModelAttribute("usuariosRegistrados") Map<String, UsuarioDTO> usuariosRegistrados) {

        ModelAndView modelAndView = new ModelAndView();

        if (usuariosRegistrados.containsKey(usuario)) {
            modelAndView.setViewName("registro");
            modelAndView.addObject("error", "El usuario ya está registrado.");
        } else {
            UsuarioDTO nuevoUsuario = new UsuarioDTO(usuario, password, rol);
            System.out.println("Registrando usuario: " + usuario + " con rol: " + rol);
            usuariosRegistrados.put(usuario, nuevoUsuario); // ✅ Se almacena correctamente

            modelAndView.setViewName("redirect:/login");
        }

        return modelAndView; 
    }

    // 📌 Cerrar sesión
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // 📌 Vista de artículos (después del login)
    @GetMapping("/articulos")
    public String mostrarArticulos(HttpSession session, Model model) {
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        return "articulos";
    }
}