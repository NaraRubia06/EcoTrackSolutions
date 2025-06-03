package br.unipar.programacaoweb.ecotracksolutions.service;

import br.unipar.programacaoweb.ecotracksolutions.model.EstacaoMonitoramento;
import br.unipar.programacaoweb.ecotracksolutions.repository.EstacaoMonitoramentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendamentoService {

    private static final Logger logger = LoggerFactory.getLogger(AgendamentoService.class);

    private final EstacaoMonitoramentoRepository estacaoRepository;

    public AgendamentoService(EstacaoMonitoramentoRepository estacaoRepository) {
        this.estacaoRepository = estacaoRepository;
    }

    // Agendado para rodar a cada 2 minutos
    @Scheduled(fixedRate = 120000)
    public void verificarEstacoesInativas() {
        logger.info("Iniciando verificação de estações inativas...");

        List<EstacaoMonitoramento> estacoes = estacaoRepository.findAll();

        for (EstacaoMonitoramento estacao : estacoes) {
            if (estacao.getStatus() == EstacaoMonitoramento.StatusEstacao.INATIVA) {
                logger.warn("[ALERTA] Estação '{}' está inativa há 2 minutos. ISSO É INACEITÁVEL!", estacao.getNome());
                logger.info("Simulando envio de notificação ao sistema de manutenção externa para a estação '{}'.", estacao.getNome());
            }
        }

        logger.info("Verificação concluída.");
    }
}
