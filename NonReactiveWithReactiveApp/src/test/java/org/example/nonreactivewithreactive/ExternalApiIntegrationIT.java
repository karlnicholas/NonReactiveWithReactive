package org.example.nonreactivewithreactive;

import org.example.nonreactivewithreactive.model.Message;
import org.example.nonreactivewithreactive.service.AppService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import java.sql.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    private Connection connection;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        // Configure application properties dynamically for containers
        registry.add("spring.datasource.url", sqlServerContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlServerContainer::getUsername);
        registry.add("spring.datasource.password", sqlServerContainer::getPassword);
        registry.add("nonreactivewithreactive.externalapi.baseurl", () -> "http://" + externalApiContainer.getHost()
                + ":" + externalApiContainer.getMappedPort(8080));
    }
    @BeforeEach
    void setUp() throws Exception {
        // Connect to the container's database
        connection = DriverManager.getConnection(
                sqlServerContainer.getJdbcUrl(),
                sqlServerContainer.getUsername(),
                sqlServerContainer.getPassword()
        );
    }
    @AfterEach
    void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testAppService() throws SQLException {
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
        // Query the database
        String query = "SELECT message FROM MESSAGE WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String name = resultSet.getString("message");
            // Assert the result
            assertEquals("Starting Message:Hello World!", name);

            preparedStatement.setInt(1, 2);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            name = resultSet.getString("message");
            // Assert the result
            assertEquals("Starting Message:Hello World!:Hello World!", name);
        }
    }
}
