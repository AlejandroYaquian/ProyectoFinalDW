package com.umg.proyectofinaldw.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.umg.proyectofinaldw.model.AdminUser;
import com.umg.proyectofinaldw.repository.AdminUserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AdminUserRepository repo;

@PostMapping("/login")
public String login(
        @RequestParam String username,
        @RequestParam String password,
        HttpSession session,
        Model model) {

    Optional<AdminUser> userOpt = repo.findByUsernameAndPassword(username, password);

    if (userOpt.isPresent() && userOpt.get().getActivo()) {
        session.setAttribute("usuario", userOpt.get());
        return "redirect:/dashboard";
    } else {
        model.addAttribute("error", "Usuario o contraseña incorrecta");
        return "login";
    }
}
}