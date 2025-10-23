package com.umg.proyectofinaldw.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.umg.proyectofinaldw.model.AdminUser;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    Optional<AdminUser> findByUsernameAndPassword(String username, String password);
        Optional<AdminUser> findByUsername(String username);
}