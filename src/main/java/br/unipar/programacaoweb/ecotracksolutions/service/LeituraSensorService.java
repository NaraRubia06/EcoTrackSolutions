package br.unipar.programacaoweb.ecotracksolutions.service;

import br.unipar.programacaoweb.ecotracksolutions.dto.EstatisticasDTO;
import br.unipar.programacaoweb.ecotracksolutions.model.Alerta;
import br.unipar.programacaoweb.ecotracksolutions.model.LeituraSensor;
import br.unipar.programacaoweb.ecotracksolutions.model.EstacaoMonitoramento;
import br.unipar.programacaoweb.ecotracksolutions.repository.AlertaRepository;
import br.unipar.programacaoweb.ecotracksolutions.repository.LeituraSensorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;

@Service
public class LeituraSensorService {

    private final LeituraSensorRepository leituraSensorRepository;
    private final AlertaRepository alertaRepository;
    private final EstacaoMonitoramentoService estacaoMonitoramentoService;

    public LeituraSensorService(LeituraSensorRepository leituraSensorRepository,
                                AlertaRepository alertaRepository,
                                EstacaoMonitoramentoService estacaoMonitoramentoService) {
        this.leituraSensorRepository = leituraSensorRepository;
        this.alertaRepository = alertaRepository;
        this.estacaoMonitoramentoService = estacaoMonitoramentoService;
    }

    // Cadastrar leitura com alerta automático
    public LeituraSensor cadastrarLeitura(LeituraSensor leitura) {
        if (leitura.getValor() == null) {
            throw new IllegalArgumentException("Valor da leitura não pode ser nulo");
        }

        LocalDateTime agora = LocalDateTime.now();
        leitura.setTimestamp(agora);

        LeituraSensor leituraSalva = leituraSensorRepository.save(leitura);

        if (foraDoIntervalo(leituraSalva)) {
            Alerta alerta = new Alerta();
            alerta.setMensagem("Valor fora do intervalo aceitável para o sensor: " + leitura.getTipoSensor().name());
            alerta.setTimestamp(agora);
            alerta.setSensor(leitura.getTipoSensor());
            alerta.setLeitura(leituraSalva);
            alertaRepository.save(alerta);
        }

        return leituraSalva;
    }

    public LeituraSensor registrarLeitura(Long estacaoId, LeituraSensor.TipoSensor tipo, float valor, String unidade) {
        LeituraSensor leitura = new LeituraSensor();

        // Busca a estação real pelo id, não cria nova
        EstacaoMonitoramento estacao = estacaoMonitoramentoService.buscarPorId(estacaoId);
        if (estacao == null) {
            throw new IllegalArgumentException("Estação de monitoramento não encontrada para id: " + estacaoId);
        }

        leitura.setEstacao(estacao);
        leitura.setTipoSensor(tipo);
        leitura.setValor(valor);
        leitura.setUnidade(unidade);

        return cadastrarLeitura(leitura);
    }

    private boolean foraDoIntervalo(LeituraSensor leitura) {
        float valor = leitura.getValor();
        return switch (leitura.getTipoSensor()) {
            case TEMPERATURA -> valor < 0 || valor > 40;
            case UMIDADE -> valor < 20 || valor > 80;
            case CO2 -> valor < 400 || valor > 800;
            case RUIDO -> valor < 30 || valor > 100;
        };
    }

    public List<LeituraSensor> buscarPorTipoEPeriodo(EstacaoMonitoramento estacao, LeituraSensor.TipoSensor tipoSensor, LocalDateTime inicio, LocalDateTime fim) {
        return leituraSensorRepository.findByEstacaoAndTipoSensorAndTimestampBetween(estacao, tipoSensor, inicio, fim);
    }

    public List<LeituraSensor> buscarPorEstacaoTipoEPeriodo(Long idEstacao, LeituraSensor.TipoSensor tipoSensor, LocalDateTime inicio, LocalDateTime fim) {
        return leituraSensorRepository.findByEstacaoAndTipoSensorAndPeriodo(idEstacao, tipoSensor, inicio, fim);
    }

    public Map<String, Double> calcularEstatisticas(Long estacaoId, LeituraSensor.TipoSensor tipo,
                                                    LocalDateTime inicio, LocalDateTime fim) {
        List<LeituraSensor> leituras = leituraSensorRepository.findByEstacaoIdAndTipoSensorAndTimestampBetween(
                estacaoId, tipo, inicio, fim);

        if (leituras.isEmpty()) {
            return Map.of("media", 0.0, "minima", 0.0, "maxima", 0.0);
        }

        DoubleSummaryStatistics stats = leituras.stream()
                .mapToDouble(LeituraSensor::getValor)
                .summaryStatistics();

        return Map.of(
                "media", stats.getAverage(),
                "minima", stats.getMin(),
                "maxima", stats.getMax()
        );
    }



    public Double buscarMediaGeralPorEstacao(Long idEstacao) {
        return leituraSensorRepository.findMediaGeralByEstacao(idEstacao);
    }
}
