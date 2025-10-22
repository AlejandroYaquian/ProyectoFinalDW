package com.umg.proyectofinaldw.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umg.proyectofinaldw.model.Pista;
import com.umg.proyectofinaldw.repository.PistaRepository;

@Service
public class PistaService {
    private final PistaRepository repo;
        
    @Autowired
    private PistaRepository pistaRepository;

    public PistaService(PistaRepository repo) {
        this.repo = repo;
    }

    public List<Pista> listar() {
        return repo.findAll();
    }

    public Optional<Pista> buscar(Long id) {
        return repo.findById(id);
    }

    public Pista guardar(Pista pista) {
        return repo.save(pista);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }


        private final ObjectMapper objectMapper = new ObjectMapper();

    public String exportarPista(Long id) throws IOException {
        Pista pista = pistaRepository.findById(id).orElseThrow();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(pista);
    }

    public void importarPista(Long id, MultipartFile file) throws IOException {
        Pista nuevaPista = objectMapper.readValue(file.getInputStream(), Pista.class);
        Pista existente = pistaRepository.findById(id).orElseThrow();

        existente.setNombre(nuevaPista.getNombre());
        existente.setConfiguracionJson(nuevaPista.getConfiguracionJson());
        pistaRepository.save(existente);
    }

    public void importarVariasPistas(MultipartFile file) throws IOException {
        List<Pista> pistas = Arrays.asList(objectMapper.readValue(file.getInputStream(), Pista[].class));
        pistaRepository.saveAll(pistas);
    }
}