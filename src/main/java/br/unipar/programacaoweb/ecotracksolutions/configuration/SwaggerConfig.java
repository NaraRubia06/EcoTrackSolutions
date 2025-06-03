package br.unipar.programacaoweb.ecotracksolutions.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EcoTrackSolution API")
                        .version("1.0")
                        .description("API RESTful para gerenciamento de estações de monitoramento ambiental," +
                                " sensores, leituras e alertas. Permite autenticação via JWT, " +
                                "controle de acesso por perfis (ADMIN e USER), geração automática de leituras e " +
                                "integração externa com serviços de clima."));

    }
}
