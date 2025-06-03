package br.unipar.programacaoweb.ecotracksolutions.controller;

import br.unipar.programacaoweb.ecotracksolutions.model.EstacaoMonitoramento;
import br.unipar.programacaoweb.ecotracksolutions.model.LeituraSensor;
import br.unipar.programacaoweb.ecotracksolutions.service.EstacaoMonitoramentoService;
import br.unipar.programacaoweb.ecotracksolutions.configuration.LeituraSensorSchedulerConfig;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/estacoes")
public class EstacaoMonitoramentoController {

    private final EstacaoMonitoramentoService service;
    private final LeituraSensorSchedulerConfig leituraSensorSchedulerConfig;

    public EstacaoMonitoramentoController(EstacaoMonitoramentoService service,
                                          LeituraSensorSchedulerConfig leituraSensorSchedulerConfig) {
        this.service = service;
        this.leituraSensorSchedulerConfig = leituraSensorSchedulerConfig;
    }

    // Criar uma estação
    @PostMapping
    public ResponseEntity<EstacaoMonitoramento> criar(@RequestBody EstacaoMonitoramento estacao) {
        EstacaoMonitoramento nova = service.salvar(estacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(nova);
    }

    // Listar todas as estações
    @GetMapping
    public ResponseEntity<List<EstacaoMonitoramento>> listar() {
        List<EstacaoMonitoramento> estacoes = service.listarTodos();
        return ResponseEntity.ok(estacoes);
    }

    // Buscar estação por ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<EstacaoMonitoramento> buscarPorId(@PathVariable Long id) {
        EstacaoMonitoramento estacao = service.buscarPorId(id);
        return ResponseEntity.ok(estacao);
    }

    // Atualizar estação
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<EstacaoMonitoramento> atualizar(@PathVariable Long id,
                                                          @Valid @RequestBody EstacaoMonitoramento atualizada) {
        EstacaoMonitoramento estacaoAtualizada = service.atualizar(id, atualizada);
        return ResponseEntity.ok(estacaoAtualizada);
    }

    // Deletar estação
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/inativas")
    public ResponseEntity<List<EstacaoMonitoramento>> listarEstacoesInativas() {
        List<EstacaoMonitoramento> estacoesInativas = service.buscarEstacoesInativas();
        return ResponseEntity.ok(estacoesInativas);
    }

    @GetMapping("/{id}/sensores/inativos")
    public ResponseEntity<Set<LeituraSensor.TipoSensor>> getSensoresInativos(@PathVariable Long id) {
        Set<LeituraSensor.TipoSensor> sensoresInativos = leituraSensorSchedulerConfig.getSensoresInativosPorEstacao(id);
        if (sensoresInativos == null || sensoresInativos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sensoresInativos);
    }

}
