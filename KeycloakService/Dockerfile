# Usa l'immagine base Alpine
FROM alpine:3.18

# Imposta la directory di lavoro
WORKDIR /opt

# Installa dipendenze necessarie
RUN echo "Inizio installazione delle dipendenze..." && \
    apk update && \
    apk add --no-cache \
    bash \
    openjdk17 \
    wget \
    curl \
    unzip \
    iputils && \
    echo "Dipendenze installate con successo!"

# Scarica e installa Keycloak
RUN echo "Inizio download e installazione di Keycloak..." && \
    wget https://github.com/keycloak/keycloak/releases/download/21.1.1/keycloak-21.1.1.tar.gz && \
    tar -xvzf keycloak-21.1.1.tar.gz && \
    mv keycloak-21.1.1 /opt/keycloak && \
    echo "Keycloak installato con successo!" && \
    rm keycloak-21.1.1.tar.gz

# Imposta il comando di default per avviare Keycloak in modalità dev
CMD ["/opt/keycloak/bin/kc.sh", "start-dev"]
