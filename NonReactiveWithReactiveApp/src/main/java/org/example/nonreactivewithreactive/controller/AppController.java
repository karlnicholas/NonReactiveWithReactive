package org.example.nonreactivewithreactive.controller;

import org.example.nonreactivewithreactive.model.Message;
import org.example.nonreactivewithreactive.service.AppService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class AppController {
    private final AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping("/")
    public String index() {
        return "Hello World!";
    }
    @GetMapping("/process")
    public Mono<Message> process() {
        // Offload blocking processWorkflow to boundedElastic
        return Mono.fromCallable(appService::processWorkflow)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
