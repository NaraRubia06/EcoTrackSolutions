package br.unipar.programacaoweb.ecotracksolutions.dto;

import br.unipar.programacaoweb.ecotracksolutions.model.EstacaoMonitoramento;
import br.unipar.programacaoweb.ecotracksolutions.model.LeituraSensor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class EstacaoMonitoramentoDTO {
    private Long id;
    private String nome;
    private Double latitude;
    private Double longitude;
    private EstacaoMonitoramento.StatusEstacao status;
    private LocalDate dataInstalacao;


}
