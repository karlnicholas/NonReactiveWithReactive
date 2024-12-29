package org.example.nonreactivewithreactive.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${nonreactivewithreactive.externalapi.baseurl}")
    private String baseUrl;
    @Bean
    WebClient webClient(WebClient.Builder webClientBuilder) {
            return webClientBuilder.baseUrl(baseUrl).build();
    }
}
