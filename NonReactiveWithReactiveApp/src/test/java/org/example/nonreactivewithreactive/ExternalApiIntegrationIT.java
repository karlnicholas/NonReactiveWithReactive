package org.example.nonreactivewithreactive;

import org.example.nonreactivewithreactive.model.Message;
import org.example.nonreactivewithreactive.service.AppService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {FlywayAutoConfiguration.class})
public class ExternalApiIntegrationIT {

    // MSSQL Testcontainer
    @Container
    private static final MSSQLServerContainer<?> sqlServerContainer =
            new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2022-latest")
                    .withPassword("yourStrong(!)Password")
                    .withInitScript("schema.sql"); // Use schema.sql for database setup

    // Mock external API Testcontainer
    @Container
    private static final GenericContainer<?> externalApiContainer =
            new GenericContainer<>("knicholas/externalapi:latest")
                    .withExposedPorts(8080);

    @Autowired
    private AppService appService; // Inject the application service to test

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        // Configure application properties dynamically for containers
        registry.add("spring.datasource.url", sqlServerContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlServerContainer::getUsername);
        registry.add("spring.datasource.password", sqlServerContainer::getPassword);
        registry.add("nonreactivewithreactive.externalapi.baseurl", () -> "http://" + externalApiContainer.getHost()
                + ":" + externalApiContainer.getMappedPort(8080));
    }

    @Test
    void testAppService() {
        // Call the app service method and verify results
        Message message = appService.processWorkflow();
        // Assertions based on the behavior of the service
        assertThat(message.getMessage()).isEqualTo("Starting Message:Hello World!");

        // Call the app service method and verify results
        message = appService.processWorkflow();
        // Assertions based on the behavior of the service
        assertThat(message.getMessage()).isEqualTo("Starting Message:Hello World!:Hello World!");

        // Call the app service method and verify results
        message = appService.processWorkflow();
        // Assertions based on the behavior of the service
        assertThat(message.getMessage()).isEqualTo("Starting Message:Hello World!:Hello World!");
    }
}

//
//import org.example.nonreactivewithreactive.model.Message;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.testcontainers.containers.GenericContainer;
//import org.testcontainers.containers.MSSQLServerContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@Testcontainers
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class ExternalApiIntegrationIT {
//
//    @Container
//    private static final GenericContainer<?> externalApiContainer =
//            new GenericContainer<>("knicholas/externalapi:latest")
//                    .withExposedPorts(8080); // External API listens on port 8080
//
//    @Container
//    private static final MSSQLServerContainer<?> sqlServerContainer =
//            new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2022-latest")
//                    .withPassword("yourStrong(!)Password")
//                    .withInitScript("schema.sql"); // The script must be on the classpath;
//    @Test
//    void testExternalApi() {
//        String externalApiBaseUrl = "http://localhost:" + externalApiContainer.getMappedPort(8081);
//
//        // Make a test call to the External API container
//        Message message = WebClient.create(externalApiBaseUrl)
//                .get()
//                .uri("/external") // Replace with the actual API endpoint
//                .retrieve()
//                .bodyToMono(Message.class)
//                .block();
//
//        // Validate the response
//        assertThat(message.getMessage()).isEqualTo("Hello World!");
//    }
//}
