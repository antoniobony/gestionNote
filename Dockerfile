# Stage 1: Build the application using Maven
FROM maven:3.6.3-openjdk-17 AS builder
WORKDIR /app

# Copy only the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application using OpenJDK
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
