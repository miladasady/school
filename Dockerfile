FROM openjdk:21-jdk-slim AS build
COPY ./target/school-0.0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

#docker ps
#docker run -p 8080:8080 6e19208c4e50


