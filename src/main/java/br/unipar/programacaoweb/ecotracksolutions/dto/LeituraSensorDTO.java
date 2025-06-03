package br.unipar.programacaoweb.ecotracksolutions.dto;

import br.unipar.programacaoweb.ecotracksolutions.model.LeituraSensor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LeituraSensorDTO {
    private Long id;
    private LeituraSensor.TipoSensor tipoSensor;
    private Float valor;
    private String unidade;
    private LocalDateTime timestamp;
    private Long estacaoId;  // Para associar com a estação

}
