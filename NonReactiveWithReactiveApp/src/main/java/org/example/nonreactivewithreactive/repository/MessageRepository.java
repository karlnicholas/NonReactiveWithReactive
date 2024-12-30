package org.example.nonreactivewithreactive.repository;

import org.example.nonreactivewithreactive.model.Message;
import org.springframework.data.repository.ListCrudRepository;

public interface MessageRepository extends ListCrudRepository<Message, Long> {
}
