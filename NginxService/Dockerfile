# Usa l'immagine base di NGINX con versione 1.25.3 e Alpine 3.18
FROM nginx:1.25.3-alpine3.18

# Rimuove le directory di configurazione e contenuti predefiniti di NGINX per evitare conflitti
RUN rm -rf /etc/nginx/conf.d/* /usr/share/nginx/html/*

# Copia una configurazione temporanea di NGINX per testare la configurazione senza HTTPS
COPY nginx.build.conf /etc/nginx/nginx.conf

# Verifica che la configurazione temporanea non abbia errori
RUN nginx -t

# Sovrascrive la configurazione temporanea con quella finale
COPY nginx.conf /etc/nginx/nginx.conf

RUN adduser -D -u 1002 -g 1002 nginx-user
## Crea directory necessarie e assegna i permessi corretti all’utente nginx (UID 101/GID 101 su Alpine)
#RUN mkdir -p /run && chown -R nginx:nginx /run \
#    && mkdir -p /var/log/nginx \
#    && chown -R nginx:nginx /var/log/nginx \
#    && chmod 700 /var/log/nginx
# Crea directory necessarie e assegna i permessi stretti all’utente nginx (UID 101/GID 101 su Alpine)
RUN mkdir -p /run /var/log/nginx \
    && chown -R 1002:1002 /run /var/log/nginx \
    && chmod 700 /run /var/log/nginx


# Esegui NGINX come utente non-root per motivi di sicurezza
USER nginx-user

# Esponi le porte 80 (HTTP) e 443 (HTTPS) per il traffico web
EXPOSE 80 443

# Esegui NGINX in modalità primo piano, per mantenere attivo il container
CMD ["nginx", "-g", "daemon off;"]







