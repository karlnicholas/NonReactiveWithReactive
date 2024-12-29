package org.example.nonreactivewithreactive.repository;

import org.example.nonreactivewithreactive.model.Message;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface MessageRepository extends ListCrudRepository<Message, Long> {
    Optional<Message> getFirstById(long l);
}
