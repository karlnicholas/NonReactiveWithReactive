package org.example.nonreactivewithreactive.service;

import org.example.nonreactivewithreactive.model.Message;
import org.example.nonreactivewithreactive.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppService {

    private final MessageRepository dbRepository;
    private final ExtenalApiService extenalApiService;

    public AppService(MessageRepository dbRepository, ExtenalApiService extenalApiService) {
        this.dbRepository = dbRepository;
        this.extenalApiService = extenalApiService;
    }

    public Message processWorkflow() {
        Message existingMessage = readFromDatabase(1L).orElse(Message.builder().message("Starting Message").build());
        return extenalApiService.callExternalApi()
                .map(externalMessage ->
                        Message.builder().message(existingMessage.getMessage() + ":" + externalMessage.getMessage()).build())
                .map(this::updateDatabase)
                .block();
    }

    public Optional<Message> readFromDatabase(Long id) {
        return dbRepository.findById(id);
    }

    public Message updateDatabase(Message entity) {
        return dbRepository.save(entity);
    }
}
