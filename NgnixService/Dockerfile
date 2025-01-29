# Usa l'immagine base Alpine
FROM alpine:3.18

# Aggiorna il sistema e installa NGINX
RUN apk update && apk add --no-cache nginx

# Crea la directory per la configurazione personalizzata di NGINX
RUN mkdir -p /etc/nginx/conf.d

# Copia la configurazione personalizzata di NGINX nella directory appropriata
COPY nginx.conf /etc/nginx/conf.d/nginx.conf

# Esponi la porta 80 per consentire il traffico HTTP
EXPOSE 80

# Comando per avviare NGINX in modalità non-daemon
CMD ["nginx", "-g", "daemon off;"]
