package br.unipar.programacaoweb.ecotracksolutions.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data // @Data já gera getter, setter, equals, hashCode e toString.

public class Alerta { //Ela registra quando uma leitura de sensor está fora de um intervalo aceitável.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensagem;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private LeituraSensor.TipoSensor sensor;

    @ManyToOne
    @JoinColumn(name = "leitura_sensor_id")
    private LeituraSensor leitura;
}

// Para registrar alertas quando leituras estão fora do intervalo esperado.