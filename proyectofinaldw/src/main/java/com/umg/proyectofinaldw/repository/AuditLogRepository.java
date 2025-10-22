package com.umg.proyectofinaldw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umg.proyectofinaldw.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> { }