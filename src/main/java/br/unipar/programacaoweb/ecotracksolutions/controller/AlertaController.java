package br.unipar.programacaoweb.ecotracksolutions.controller;

import br.unipar.programacaoweb.ecotracksolutions.model.Alerta;
import br.unipar.programacaoweb.ecotracksolutions.service.AlertaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    // Listar todos os alertas
    @GetMapping
    public ResponseEntity<List<Alerta>> listarAlertas() {
        List<Alerta> alertas = alertaService.listarTodos();
        return ResponseEntity.ok(alertas);
    }

    // Buscar alerta por id
    @GetMapping("/{id}")
    public ResponseEntity<Alerta> buscarPorId(@PathVariable Long id) {
        return alertaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
