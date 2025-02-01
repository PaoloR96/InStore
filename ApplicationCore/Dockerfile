# Usa un'immagine base di OpenJDK
FROM openjdk:17-jdk-slim
VOLUME /tmp

# Copia il file JAR generato da Maven nella directory di lavoro
COPY target/ApplicationCore-0.0.1-SNAPSHOT.jar applicationcore.jar

# Installazione di strumenti di rete (ping, wget, curl)
RUN echo "Inizio installazione di iputils-ping..." && \
    apt-get update && \
    apt-get install -y iputils-ping wget curl && \
    echo "Strumenti installati con successo!" && \
    rm -rf /var/lib/apt/lists/*

# Comando di avvio del servizio
ENTRYPOINT ["java", "-jar", "/applicationcore.jar"]
