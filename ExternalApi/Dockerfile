# Use an official Java 21 runtime as the base image
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot jar file into the container
COPY target/*.jar app.jar

# Expose the port that the Spring Boot application runs on (default is 8080)
EXPOSE 8080

# Define the entry point to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
