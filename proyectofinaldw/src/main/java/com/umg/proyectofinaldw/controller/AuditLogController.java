package com.umg.proyectofinaldw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.umg.proyectofinaldw.service.AuditLogService;

@Controller
@RequestMapping("/logs")
public class AuditLogController {

    private final AuditLogService service;

    public AuditLogController(AuditLogService service) {
        this.service = service;
    }

    @GetMapping
    public String listarLogs(Model model) {
        model.addAttribute("logs", service.listar());
        return "logs/list"; // templates/logs/list.html
    }
}