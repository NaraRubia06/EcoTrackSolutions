package br.unipar.programacaoweb.ecotracksolutions.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class EstacaoMonitoramento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Double latitude;

    private Double longitude;

    @Enumerated(EnumType.STRING)
    private StatusEstacao status;

    private LocalDate dataInstalacao;


    @Column(name = "temperatura_atual")
    private Double temperaturaAtual;

    @Column(name = "umidade")
    private Integer umidade;

    @Column(name = "condicao_climatica")
    private String condicaoClimatica;


    @OneToMany(mappedBy = "estacao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<LeituraSensor> leituras = new ArrayList<>();


    public enum StatusEstacao {
        ATIVA,
        INATIVA,
        MANUTENCAO
    }
}
// EstacaoMonitoramento — Representa as estações, com id, nome, coordenadas, status, data instalação e lista de leituras.