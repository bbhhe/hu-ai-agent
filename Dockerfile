FROM openjdk:21-slim

WORKDIR /app

# Copy Gradle Wrapper files and project files
COPY gradle ./gradle
COPY gradlew .
COPY gradlew.bat .
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

# Ensure Gradle Wrapper is executable
RUN chmod +x gradlew

# Build the project using Gradle Wrapper
RUN ./gradlew build -x test

# Expose the application port
EXPOSE 8123

# Start the application with production profile
CMD ["java", "-jar", "/app/build/libs/hu-ai-agent-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]