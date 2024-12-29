package org.example.nonreactivewithreactive.service;

import org.example.nonreactivewithreactive.model.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ExtenalApiService {
    private final WebClient webClient;

    public ExtenalApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Message> callExternalApi() {
        return webClient.get()
                .uri("/external")
                .retrieve()
                .bodyToMono(Message.class);
    }
}
