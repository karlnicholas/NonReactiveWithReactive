package org.example.nonreactivewithreactive.service;

import org.example.nonreactivewithreactive.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExternalApiServiceTest {
    @InjectMocks
    private ExtenalApiService extenalApiService;

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec; // Use raw type
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;       // Use raw type
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        // Mock WebClient interactions using raw types
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/external")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Message.class))
                .thenReturn(Mono.just(Message.builder().message("External Response").build()));
    }

    @Test
    void testProcessWorkflow() {
        // Act: Call the service method
        Message result = extenalApiService.callExternalApi().block();

        // Assert: Validate the result
        assertEquals("External Response", result.getMessage());

        // Verify interactions
        verify(webClient.get()).uri("/external");
    }
}
