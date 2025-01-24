# Usa l'immagine di MySQL come base
FROM mysql:latest

# Imposta variabili d'ambiente per MySQL
ENV MYSQL_ROOT_PASSWORD=rootpassword
ENV MYSQL_DATABASE=InStore_db
ENV MYSQL_PASSWORD=secret
#RUN apt update && apt install -y iputils-ping curl
# Aggiorna il sistema e installa le dipendenze di base
RUN apt-get update && apt-get install -y \
    curl \
    wget \
    git \
    && rm -rf /var/lib/apt/lists/*

