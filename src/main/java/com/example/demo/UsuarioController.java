package com.example.demo;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuarioController {

    @Autowired
    private ServiciodbgInterface dao;
    
    @GetMapping("/formulario")
    public String mostrarFormulario() {
        return "formulario"; // Muestra el formulario HTML
    }
    
    @PostMapping("/formulario")
    public String procesarFormulario(
            @RequestParam String nombreusuario,
            @RequestParam String email,
            @RequestParam String nombre,
            @RequestParam String apellidos,
            HttpSession session,
            HttpServletResponse response,
            Model model) {

        // Crear el objeto Usuario
        Usuario usuario = new Usuario();
        usuario.setNombreusuario(nombreusuario);
        usuario.setEmail(email);
        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);

        // Guardar en la sesión
        session.setAttribute("usuario", usuario);

        // Guardar en una cookie
        Cookie cookie = new Cookie("nombreusuario", nombreusuario);
        cookie.setMaxAge(60 * 60 * 24 * 7); // 7 días
        cookie.setPath("/");
        response.addCookie(cookie);

        // Aquí se hace una redirección explícita a /usuario_guardado con GET
        return "redirect:/usuario_guardado";
    }
    
    @GetMapping("/usuario_guardado")
    public String mostrarUsuarioGuardado(HttpSession session, HttpServletRequest request, Model model) {
        // Obtener el usuario de la sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Si no existe el usuario en la sesión, redirigir al formulario
        if (usuario == null) {
            return "redirect:/formulario";
        }

        // Obtener las cookies (si existen)
        String cookieNombre = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("nombreusuario".equals(cookie.getName())) {
                    cookieNombre = cookie.getValue();
                }
            }
        }

        // Pasar los datos al modelo
        model.addAttribute("usuario", usuario);
        model.addAttribute("cookieNombre", cookieNombre);

        return "usuario_guardado";  // Asegúrate de que el archivo 'usuario_guardado.html' existe
    }
    // Método para mostrar la página de login
    @GetMapping("/login")
    public String mostrarLogin(HttpSession sesion, Model model) {
        Userlogin user = (Userlogin) sesion.getAttribute("user");

        if (user == null) {
            return "login"; // Si el usuario no está logueado, muestra la página de login
        } else {
            if (user.getEs_admin() == 1) {
                // Si el usuario es administrador, muestra la lista de usuarios
            	model.addAttribute("usuarios", dao.getAllUsers());
                return "admin"; // Página para el admin
            } else {
                model.addAttribute("usuario", user.getNombre());
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
                // Si es admin, redirige a la página de datos del usuario
                model.addAttribute("usuarios", dao.getAllUsers());
                return "datosusuario"; // Redirige a la vista de datos de usuarios
            } else {
                model.addAttribute("usuario", name);
                return "articulos"; // Página para el usuario regular
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
            return "redirect:/login"; // Redirige a la página de login después del registro
        }
    }
 // Método para procesar el formulario y guardar el usuario en la base de datos
    @PostMapping("/guardarUsuario")
    public String guardarUsuario(HttpServletRequest req, Model model) {
        // Recibir los datos del formulario
        String nombreUsuario = req.getParameter("nombreusuario");
        String password = req.getParameter("password");
        int esAdmin = Integer.parseInt(req.getParameter("es_admin"));

        System.out.println("Recibiendo datos del formulario:");
        System.out.println("Nombre Usuario: " + nombreUsuario);
        System.out.println("Contraseña: " + password);
        System.out.println("Rol: " + esAdmin);

        // Verifica si el nombre de usuario ya existe
        if (dao.existeusu(nombreUsuario) != null) {
            model.addAttribute("exist", true); // Si el usuario ya existe
            return "datosusuario"; // Volver al formulario de datos
        } else {
            // Si no existe, crea el usuario
            dao.crearUsuario(nombreUsuario, password, esAdmin);
            
            // Redirige a una vista de éxito o a login
            return "redirect:/login"; // O redirige a login o a donde sea necesario
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

        model.addAttribute("usuario", usuario.getNombre());
        return "articulos"; // Muestra la página de artículos
    }
    
}
