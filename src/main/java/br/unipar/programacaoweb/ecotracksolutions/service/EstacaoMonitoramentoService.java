package br.unipar.programacaoweb.ecotracksolutions.service;

import br.unipar.programacaoweb.ecotracksolutions.exception.EstacaoNotFoundException;
import br.unipar.programacaoweb.ecotracksolutions.model.ClimaResponse;
import br.unipar.programacaoweb.ecotracksolutions.model.EstacaoMonitoramento;
import br.unipar.programacaoweb.ecotracksolutions.repository.EstacaoMonitoramentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstacaoMonitoramentoService {

    private final EstacaoMonitoramentoRepository repository;

    private final ClimaService climaService;

    public EstacaoMonitoramentoService(EstacaoMonitoramentoRepository repository,   ClimaService climaService) {
        this.repository = repository;
        this.climaService = climaService;
    }

    public EstacaoMonitoramento salvar(EstacaoMonitoramento estacao) {
        // Buscar dados da API externa
        var clima = climaService.buscarClimaPorCoordenadas(estacao.getLatitude(), estacao.getLongitude());

        if (clima != null && clima.getMain() != null && !clima.getWeather().isEmpty()) {
            estacao.setTemperaturaAtual(clima.getMain().getTemp());
            estacao.setUmidade(clima.getMain().getHumidity());
            estacao.setCondicaoClimatica(clima.getWeather().get(0).getDescription());
        }

        return repository.save(estacao);
    }

    public List<EstacaoMonitoramento> listarTodos() {
        return repository.findAll();
    }

    public EstacaoMonitoramento buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EstacaoNotFoundException("Estação não encontrada com ID: " + id));
    }

    public EstacaoMonitoramento atualizar(Long id, EstacaoMonitoramento atualizada) {
        EstacaoMonitoramento existente = buscarPorId(id);
        existente.setNome(atualizada.getNome());
        existente.setLatitude(atualizada.getLatitude());
        existente.setLongitude(atualizada.getLongitude());
        existente.setStatus(atualizada.getStatus());
        existente.setDataInstalacao(atualizada.getDataInstalacao());
        return repository.save(existente);
    }

    public void deletar(Long id) {
        EstacaoMonitoramento existente = buscarPorId(id);
        repository.delete(existente);
    }

    public boolean atualizarStatus(Long id, EstacaoMonitoramento.StatusEstacao status) {
        Optional<EstacaoMonitoramento> estacaoOpt = repository.findById(id);
        if (estacaoOpt.isEmpty()) {
            return false;
        }
        EstacaoMonitoramento estacao = estacaoOpt.get();
        estacao.setStatus(status);
        repository.save(estacao);
        return true;
    }

    // Buscar estações inativas (usado pelo scheduler)
    public List<EstacaoMonitoramento> buscarEstacoesInativas() {
        return repository.findByStatus(EstacaoMonitoramento.StatusEstacao.INATIVA);
    }


}
