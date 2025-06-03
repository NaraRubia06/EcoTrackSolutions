package br.unipar.programacaoweb.ecotracksolutions.configuration;

import br.unipar.programacaoweb.ecotracksolutions.model.EstacaoMonitoramento;
import br.unipar.programacaoweb.ecotracksolutions.model.LeituraSensor;
import br.unipar.programacaoweb.ecotracksolutions.repository.EstacaoMonitoramentoRepository;
import br.unipar.programacaoweb.ecotracksolutions.service.LeituraSensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LeituraSensorSchedulerConfig {

    private static final Logger logger = LoggerFactory.getLogger(LeituraSensorSchedulerConfig.class);

    private final EstacaoMonitoramentoRepository estacaoRepository;
    private final LeituraSensorService leituraService;
    private final Random random = new Random();

    //  ConcurrentHashMap para segurança em concorrência
    private final Map<Long, Set<LeituraSensor.TipoSensor>> sensoresInativosPorEstacao = new ConcurrentHashMap<>();

    public LeituraSensorSchedulerConfig(EstacaoMonitoramentoRepository estacaoRepository,
                                        LeituraSensorService leituraService) {
        this.estacaoRepository = estacaoRepository;
        this.leituraService = leituraService;
    }

    // Executa a cada 2 minutos (120.000 ms)
    @Scheduled(fixedRate = 120000)
    public void executarTarefaDeMonitoramento() {
        logger.info("Iniciando tarefa de monitoramento de estações...");
        List<EstacaoMonitoramento> estacoes = estacaoRepository.findAll();

        for (EstacaoMonitoramento estacao : estacoes) {
            if (estacao.getStatus() == EstacaoMonitoramento.StatusEstacao.ATIVA) {
                gerarLeiturasParaEstacao(estacao);
            } else {
                registrarEstacaoInativa(estacao);
                // Limpar sensores inativos para esta estação pois está inativa
                sensoresInativosPorEstacao.remove(estacao.getId());
            }
        }
    }

    private void gerarLeiturasParaEstacao(EstacaoMonitoramento estacao) {
        Set<LeituraSensor.TipoSensor> sensoresInativos = sensoresInativosPorEstacao
                .getOrDefault(estacao.getId(), Collections.emptySet());

        for (LeituraSensor.TipoSensor tipo : LeituraSensor.TipoSensor.values()) {
            if (sensoresInativos.contains(tipo)) {
                logger.warn("Sensor {} está INATIVO na estação {}", tipo, estacao.getNome());
                continue;
            }

            float valor = gerarValorAleatorio(tipo);
            String unidade = getUnidade(tipo);
            leituraService.registrarLeitura(estacao.getId(), tipo, valor, unidade);

            logger.info("Leitura gerada automaticamente: Estação={}, Tipo={}, Valor={}, Unidade={}",
                    estacao.getNome(), tipo.name(), valor, unidade);
        }
    }

    private void registrarEstacaoInativa(EstacaoMonitoramento estacao) {
        logger.warn("[ALERTA] Estação '{}' está inativa há 2 minutos.", estacao.getNome());
        logger.info("Simulando envio de notificação ao sistema de manutenção externa para a estação '{}'.", estacao.getNome());
    }

    private float gerarValorAleatorio(LeituraSensor.TipoSensor tipo) {
        return switch (tipo) {
            case TEMPERATURA -> -10 + random.nextFloat() * 70; // -10 a 60
            case UMIDADE -> random.nextFloat() * 100;
            case CO2 -> 250 + random.nextFloat() * 1200;
            case RUIDO -> 20 + random.nextFloat() * 120;
        };
    }

    private String getUnidade(LeituraSensor.TipoSensor tipo) {
        return switch (tipo) {
            case TEMPERATURA -> "°C";
            case UMIDADE -> "%";
            case CO2 -> "ppm";
            case RUIDO -> "dB";
        };
    }

    // Inativa sensores aleatoriamente em estações ativas a cada 1 minuto
    @Scheduled(fixedRate = 60000)
    public void inativarSensoresAleatoriamente() {
        logger.info("Executando inativação aleatória de sensores...");
        List<EstacaoMonitoramento> estacoesAtivas = estacaoRepository.findByStatus(EstacaoMonitoramento.StatusEstacao.ATIVA);
        if (estacoesAtivas.isEmpty()) {
            logger.info("Nenhuma estação ativa encontrada para inativar sensores.");
            return;
        }

        for (EstacaoMonitoramento estacao : estacoesAtivas) {
            int quantidade = random.nextInt(2) + 1;

            Set<LeituraSensor.TipoSensor> inativos = sensoresInativosPorEstacao
                    .computeIfAbsent(estacao.getId(), id -> ConcurrentHashMap.newKeySet());

            List<LeituraSensor.TipoSensor> sensoresDisponiveis = new ArrayList<>(List.of(LeituraSensor.TipoSensor.values()));
            sensoresDisponiveis.removeAll(inativos);

            if (sensoresDisponiveis.isEmpty()) {
                logger.info("Todos os sensores já estão inativos na estação {}", estacao.getNome());
                continue;
            }

            for (int i = 0; i < quantidade && !sensoresDisponiveis.isEmpty(); i++) {
                int index = random.nextInt(sensoresDisponiveis.size());
                LeituraSensor.TipoSensor tipo = sensoresDisponiveis.get(index);
                inativos.add(tipo);
                sensoresDisponiveis.remove(index);

                logger.warn("[INATIVAÇÃO AUTOMÁTICA] Sensor {} foi inativado na estação {}", tipo, estacao.getNome());
            }
        }
    }

    public Set<LeituraSensor.TipoSensor> getSensoresInativosPorEstacao(Long estacaoId) {
        return sensoresInativosPorEstacao.getOrDefault(estacaoId, Collections.emptySet());
    }

}
