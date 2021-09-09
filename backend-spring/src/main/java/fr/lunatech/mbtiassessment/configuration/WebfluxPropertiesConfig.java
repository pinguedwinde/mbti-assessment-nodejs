package fr.lunatech.mbtiassessment.configuration;

import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebfluxPropertiesConfig {
    @Bean
    public WebFluxProperties webFluxProperties() {
        return new WebFluxProperties();
    }
}
