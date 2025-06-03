package br.unipar.programacaoweb.ecotracksolutions.dto;

import br.unipar.programacaoweb.ecotracksolutions.model.LeituraSensor.TipoSensor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AlertaDTO {
    private Long id;
    private String mensagem;
    private LocalDateTime timestamp;
    private TipoSensor sensor;
    private Long leituraId; // associar ao ID da leitura, se quiser expor
}
