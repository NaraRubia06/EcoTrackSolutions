package br.unipar.programacaoweb.ecotracksolutions.dto;

public class EstatisticasDTO {

    private Double minimo;
    private Double maximo;
    private Double media;

    public EstatisticasDTO(Double minimo, Double maximo, Double media) {
        this.minimo = minimo;
        this.maximo = maximo;
        this.media = media;
    }

    public Double getMinimo() {
        return minimo;
    }

    public Double getMaximo() {
        return maximo;
    }

    public Double getMedia() {
        return media;
    }

    public void setMinimo(Double minimo) {
        this.minimo = minimo;
    }

    public void setMaximo(Double maximo) {
        this.maximo = maximo;
    }

    public void setMedia(Double media) {
        this.media = media;
    }
}