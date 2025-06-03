package br.unipar.programacaoweb.ecotracksolutions.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class LeituraSensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSensor tipoSensor;

    @Column(nullable = false)
    private Float valor;

    private String unidade;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "estacao_id")
    private EstacaoMonitoramento estacao;

    public enum TipoSensor {
        TEMPERATURA,
        UMIDADE,
        CO2,
        RUIDO
    }
}

//  Representa leituras com id, tipo sensor, valor, unidade, timestamp, e referência para estação.