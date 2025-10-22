package com.umg.proyectofinaldw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.umg.proyectofinaldw.model.AdminUser;
import com.umg.proyectofinaldw.repository.AdminUserRepository;

@Service
public class AdminUserService {
    private final AdminUserRepository repo;

    public AdminUserService(AdminUserRepository repo) {
        this.repo = repo;
    }

    public List<AdminUser> listar() { return repo.findAll(); }
    public Optional<AdminUser> buscar(Long id) { return repo.findById(id); }
    public AdminUser guardar(AdminUser user) { return repo.save(user); }
    public void eliminar(Long id) { repo.deleteById(id); }
}