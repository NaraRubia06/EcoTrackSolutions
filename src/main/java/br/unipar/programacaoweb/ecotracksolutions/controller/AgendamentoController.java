package br.unipar.programacaoweb.ecotracksolutions.controller;

import br.unipar.programacaoweb.ecotracksolutions.service.AgendamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agendamento")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping("/verificar-estacoes-inativas")
    public ResponseEntity<String> executarVerificacaoManual() {
        agendamentoService.verificarEstacoesInativas();
        return ResponseEntity.ok("Verificação manual concluída com sucesso.");
    }
}
