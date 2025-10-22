package com.umg.proyectofinaldw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.umg.proyectofinaldw.model.AdminUser;
import com.umg.proyectofinaldw.repository.AdminUserRepository;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private AdminUserRepository repo;

    @GetMapping
    public String listarUsuarios(Model model) {
        List<AdminUser> usuarios = repo.findAll();
        model.addAttribute("usuarios", usuarios);
        return "admin/users";
    }

    @GetMapping("/nuevo")
    public String nuevoUsuario(Model model) {
        model.addAttribute("usuario", new AdminUser());
        return "admin/user_form";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        AdminUser u = repo.findById(id).orElseThrow();
        model.addAttribute("usuario", u);
        return "admin/user_form";
    }

@PostMapping("/guardar")
public String guardarUsuario(@ModelAttribute AdminUser usuario) {
    if (usuario.getId() != null) {
        // Es edición
        AdminUser existente = repo.findById(usuario.getId()).orElseThrow();
        existente.setNombre(usuario.getNombre());
        existente.setUsername(usuario.getUsername());
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            existente.setPassword(usuario.getPassword());
        }
        existente.setActivo(usuario.getActivo());
        repo.save(existente); // UPDATE
    } else {
        // Nuevo usuario
        repo.save(usuario); // INSERT
    }
    return "redirect:/admin/users";
}


    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/admin/users";
    }
}
