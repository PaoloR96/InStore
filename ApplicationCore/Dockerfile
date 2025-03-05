# Usa un'immagine base di OpenJDK
FROM openjdk:17-jdk-slim
LABEL authors="Gaetano"

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar","/app.jar"]
