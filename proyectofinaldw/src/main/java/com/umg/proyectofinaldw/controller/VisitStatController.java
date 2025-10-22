package com.umg.proyectofinaldw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.umg.proyectofinaldw.service.VisitStatService;

@Controller
@RequestMapping("/stats")
public class VisitStatController {

    private final VisitStatService service;

    public VisitStatController(VisitStatService service) {
        this.service = service;
    }

    @GetMapping
    public String listarStats(Model model) {
        model.addAttribute("stats", service.listar());
        return "stats/list";
    }

    
}