package br.unipar.programacaoweb.ecotracksolutions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EcoTrackSolutionsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcoTrackSolutionsApplication.class, args);
    }

}
