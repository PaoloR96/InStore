# Usa un'immagine Alpine come base per la configurazione
FROM alpine:3.18

# Aggiungi i pacchetti necessari per Vault e strumenti di configurazione
# - curl: per scaricare file
# - unzip: per estrarre il file binario di Vault
# - bash: per eseguire script bash
# - jq: per elaborare file JSON
RUN apk add --no-cache curl unzip bash jq

# Imposta la versione di Vault da installare
ENV VAULT_VERSION=1.13.1

# Scarica Vault dalla pagina ufficiale di HashiCorp
# - Scarica il file zip nella directory temporanea
# - Estrai il binario di Vault e lo posiziona nella directory bin di sistema
# - Rimuove il file zip per pulire lo spazio
# - Crea la directory per i file di configurazione
RUN curl -fsSL https://releases.hashicorp.com/vault/${VAULT_VERSION}/vault_${VAULT_VERSION}_linux_amd64.zip -o /tmp/vault.zip && \
    unzip /tmp/vault.zip -d /usr/local/bin/ && \
    rm /tmp/vault.zip && \
    mkdir -p /vault-config

# Imposta la directory di lavoro dove si trovano i file di configurazione
WORKDIR /vault-config

# Copia i file di configurazione dall'host al container
COPY vault-config /vault-config

# Assegna i permessi corretti ai file e alle directory:
# - La directory scripts è leggibile/eseguibile solo dall'utente root (700)
# - Il file secrets.json è leggibile solo dall'utente root (600)
RUN chmod -R 700 /vault-config/scripts && \
    chmod 600 /vault-config/secrets/secrets.json

# Espone la porta 8200, che è la porta standard usata da Vault
EXPOSE 8200

# Comando di default:
# - Avvia il server Vault con il file di configurazione specificato
# - Attende 5 secondi per l'avvio del server
# - Esegue lo script per inizializzare Vault (init-vault.sh)
# - Attende che i processi terminino
CMD ["bash", "-c", "vault server -config=/vault-config/config.hcl & sleep 5 && /vault-config/scripts/init-vault.sh && wait"]
