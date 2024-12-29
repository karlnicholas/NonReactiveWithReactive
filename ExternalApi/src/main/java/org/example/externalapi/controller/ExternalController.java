package org.example.externalapi.controller;

import org.example.externalapi.model.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ExternalController {
    @GetMapping("/external")
    public Mono<Message> external() {
        return Mono.just(Message.builder().message("Hello World!").build());
    }
}
