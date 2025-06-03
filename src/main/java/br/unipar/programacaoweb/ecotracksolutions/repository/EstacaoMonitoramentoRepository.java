package br.unipar.programacaoweb.ecotracksolutions.repository;

import br.unipar.programacaoweb.ecotracksolutions.model.EstacaoMonitoramento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EstacaoMonitoramentoRepository extends JpaRepository<EstacaoMonitoramento, Long> {
    List<EstacaoMonitoramento> findByStatus(EstacaoMonitoramento.StatusEstacao status);
}

