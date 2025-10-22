package com.umg.proyectofinaldw.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.umg.proyectofinaldw.model.AuditLog;
import com.umg.proyectofinaldw.repository.AuditLogRepository;

@Service
public class AuditLogService {
    private final AuditLogRepository repo;

    public AuditLogService(AuditLogRepository repo) { this.repo = repo; }

    public List<AuditLog> listar() { return repo.findAll(); }

    public AuditLog guardar(AuditLog log) { return repo.save(log); }

    public void registrar(String quien, String accion, String detalles) {
        AuditLog log = new AuditLog();
        log.setFechaHora(LocalDateTime.now());
        log.setAccion(accion);
        log.setDetalles(detalles);
        repo.save(log);
    }
}
