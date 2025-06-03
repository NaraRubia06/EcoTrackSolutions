package br.unipar.programacaoweb.ecotracksolutions.controller;

import br.unipar.programacaoweb.ecotracksolutions.dto.EstatisticasDTO;
import br.unipar.programacaoweb.ecotracksolutions.model.EstacaoMonitoramento;
import br.unipar.programacaoweb.ecotracksolutions.model.LeituraSensor;
import br.unipar.programacaoweb.ecotracksolutions.service.EstacaoMonitoramentoService;
import br.unipar.programacaoweb.ecotracksolutions.service.LeituraSensorService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leituras")
public class LeituraSensorController {

    private final LeituraSensorService leituraSensorService;
    private final EstacaoMonitoramentoService estacaoService;

    public LeituraSensorController(LeituraSensorService leituraSensorService, EstacaoMonitoramentoService estacaoService) {
        this.leituraSensorService = leituraSensorService;
        this.estacaoService = estacaoService;
    }

    // POST: cadastrar leitura
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> cadastrarLeitura(@RequestBody LeituraSensor leitura) {
        EstacaoMonitoramento estacao = estacaoService.buscarPorId(leitura.getEstacao().getId());
        if (estacao == null) {
            return ResponseEntity.badRequest().body("Estação não encontrada");
        }
        leitura.setEstacao(estacao);
        LeituraSensor salva = leituraSensorService.cadastrarLeitura(leitura);
        return ResponseEntity.ok(salva);
    }

    // GET: leituras por tipo de sensor e período
    @GetMapping("/{estacaoId}/{tipoSensor}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<List<LeituraSensor>> consultarPorTipoEPeriodo(
            @PathVariable Long estacaoId,
            @PathVariable LeituraSensor.TipoSensor tipoSensor,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {

        EstacaoMonitoramento estacao = estacaoService.buscarPorId(estacaoId);
        if (estacao == null) {
            return ResponseEntity.badRequest().build();
        }

        List<LeituraSensor> leituras = leituraSensorService.buscarPorTipoEPeriodo(estacao, tipoSensor, inicio, fim);
        return ResponseEntity.ok(leituras);
    }

    //  estatísticas (mínima, máxima, média) por tipo de sensor, estação e período
    @GetMapping("/{id}/sensores/{tipo}/estatisticas")
    public ResponseEntity<Map<String, Double>> getEstatisticasSensor(
            @PathVariable Long id,
            @PathVariable LeituraSensor.TipoSensor tipo,
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {

        Map<String, Double> estatisticas = leituraSensorService.calcularEstatisticas(id, tipo, inicio, fim);
        return ResponseEntity.ok(estatisticas);
    }


    //  média geral de todos os sensores de uma estação
    @GetMapping("/{estacaoId}/media-geral")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Double> consultarMediaGeral(@PathVariable Long estacaoId) {
        Double media = leituraSensorService.buscarMediaGeralPorEstacao(estacaoId);
        return ResponseEntity.ok(media);
    }
}
