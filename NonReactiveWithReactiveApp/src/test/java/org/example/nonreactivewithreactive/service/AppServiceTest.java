package org.example.nonreactivewithreactive.service;

import org.example.nonreactivewithreactive.model.Message;
import org.example.nonreactivewithreactive.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppServiceTest {

    @Mock
    private MessageRepository dbRepository;

    @Mock
    private ExtenalApiService extenalApiService;

    @InjectMocks
    private AppService appService;

    @Test
    void testProcessWorkflow() {
        // Arrange: Mock database interactions
        Message existingMessage = Message.builder().id(1L).message("Starting Message").build();
        when(extenalApiService.callExternalApi()).thenReturn(Mono.just(Message.builder().id(1L).message("External Response").build()));
        when(dbRepository.findById(1L)).thenReturn(Optional.of(existingMessage));
        when(dbRepository.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: Call the service method
        Message result = appService.processWorkflow();

        // Assert: Validate the result
        assertEquals("Starting Message:External Response", result.getMessage());

        // Verify interactions
        verify(dbRepository).findById(1L);
        verify(dbRepository).save(any(Message.class));
    }
}
