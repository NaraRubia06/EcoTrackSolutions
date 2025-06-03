package br.unipar.programacaoweb.ecotracksolutions.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClimaResponseDTO {

    @JsonProperty("main")
    private MainInfo main;

    @JsonProperty("weather")
    private List<WeatherInfo> weather;

    public MainInfo getMain() {
        return main;
    }

    public List<WeatherInfo> getWeather() {
        return weather;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MainInfo {
        private Double temp;
        private Integer humidity;

        public Double getTemp() {
            return temp;
        }

        public Integer getHumidity() {
            return humidity;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherInfo {
        private String description;

        public String getDescription() {
            return description;
        }
    }
}
