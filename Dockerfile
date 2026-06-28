# Build stage using official Maven image with JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Compile and package the application
RUN mvn clean package -DskipTests

# Run stage using official lightweight JRE 17 image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the compiled JAR file from the build stage
COPY --from=build /app/target/bloodlink-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
