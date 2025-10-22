package com.umg.proyectofinaldw.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umg.proyectofinaldw.model.Pista;
import com.umg.proyectofinaldw.repository.PistaRepository;
import com.umg.proyectofinaldw.service.AuditLogService;
import com.umg.proyectofinaldw.service.PistaService;

@Controller
@RequestMapping("/pistas")
public class PistaController {

    @Autowired
    private PistaRepository repo;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private PistaService pistaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pistas", repo.findAll());
        return "pistas/list";
    }

    @GetMapping("/nuevo")
    public String nuevaPista(Model model) {
        model.addAttribute("pista", new Pista());
        return "pistas/form";
    }

    @GetMapping("/editar/{id}")
    public String editarPista(@PathVariable Long id, Model model) {
        Pista p = repo.findById(id).orElseThrow();
        model.addAttribute("pista", p);
        return "pistas/form";
    }

    @PostMapping("/guardar")
    public String guardarPista(@ModelAttribute Pista pista) {
        boolean esNuevo = (pista.getId() == null);
        repo.save(pista);

        String usuario = "admin";
        String accion = esNuevo ? "Creación de pista" : "Edición de pista";
        String detalles = "Pista: " + pista.getNombre();
        auditLogService.registrar(usuario, accion, detalles);

        return "redirect:/pistas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPista(@PathVariable Long id) {
        Pista p = repo.findById(id).orElseThrow();
        repo.deleteById(id);

        String usuario = "admin";
        auditLogService.registrar(usuario, "Eliminación de pista", "Pista: " + p.getNombre());

        return "redirect:/pistas";
    }

    @GetMapping("/jugar")
    public String vistaJuego(Model model) {
        model.addAttribute("pistas", repo.findAll());
        return "jugar";
    }

    @GetMapping("/config/{id}")
    @ResponseBody
    public Map<String, Object> obtenerConfiguracion(@PathVariable Long id) {
        Pista p = repo.findById(id).orElseThrow();
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("configuracion", p.getConfiguracionJson());
        return respuesta;
    }

    @GetMapping("/exportar/{id}")
    public ResponseEntity<ByteArrayResource> exportarPista(@PathVariable Long id) throws IOException {
        String json = pistaService.exportarPista(id);
        ByteArrayResource resource = new ByteArrayResource(json.getBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pista_" + id + ".json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(resource);
    }

    @PostMapping("/importar/{id}")
    public String importarPista(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        pistaService.importarPista(id, file);
        return "redirect:/pistas";
    }

    @PostMapping("/importar")
    public String importarNuevaPista(@RequestParam("file") MultipartFile archivo) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Pista pista = mapper.readValue(archivo.getInputStream(), Pista.class);

        pista.setId(null);
        repo.save(pista);

        return "redirect:/pistas";
    }

}
