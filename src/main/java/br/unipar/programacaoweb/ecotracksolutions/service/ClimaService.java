package br.unipar.programacaoweb.ecotracksolutions.service;

import br.unipar.programacaoweb.ecotracksolutions.dto.ClimaResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClimaService {

    @Value("${openweather.api.key}")
    private String apiKey;

    public ClimaResponseDTO buscarClimaPorCoordenadas(double lat, double lon) {
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=metric&lang=pt_br&appid=%s",
                lat, lon, apiKey);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, ClimaResponseDTO.class);
    }
}
