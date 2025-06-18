FROM openjdk:17-jdk-slim

LABEL authors="Gaetano"
# Crea un utente non-root e una cartella per l'app
#RUN groupadd -r app && useradd -r -g app app && \
#    mkdir -p /app && chown -R app:app /app

## Crea un utente non-root e una cartella per l'app
#RUN groupadd -r app && useradd -r -g app app && \
#    mkdir -p /app && chown -R app:app /app && \
#    mkdir -p /var/log/spring && chown -R app:app /var/log/spring && chmod  700 /var/log/spring

# Crea un utente non-root e cartelle con accesso esclusivo all'utente `app`
RUN adduser -D -u 1002 -g 1002 app

RUN mkdir -p /app /var/log/spring && \
    chown -R app:app /app /var/log/spring && \
    chmod 700 /app /var/log/spring

# Imposta la working directory
WORKDIR /app
# Copia il JAR con i giusti permessi
COPY --chown=app:app target/*.jar app.jar
# Esegui il container come utente non-root
USER app
# Espone la porta applicativa
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]