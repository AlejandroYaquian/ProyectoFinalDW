package com.umg.proyectofinaldw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.umg.proyectofinaldw.model.VisitStat;
import com.umg.proyectofinaldw.repository.VisitStatRepository;

@Service
public class VisitStatService {
    private final VisitStatRepository repo;

    public VisitStatService(VisitStatRepository repo) { this.repo = repo; }

    public List<VisitStat> listar() { return repo.findAll(); }
    public VisitStat guardar(VisitStat stat) { return repo.save(stat); }
}