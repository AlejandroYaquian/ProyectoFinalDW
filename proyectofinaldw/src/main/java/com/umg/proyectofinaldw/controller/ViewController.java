package com.umg.proyectofinaldw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/dashboard")
    public String home() {
        return "dashboard";
    }


        @GetMapping("/auth/login")
    public String login() {
        return "login";
    }
}