FROM keycloak/keycloak:26.0.4
LABEL authors="Gaetano"

ENV KC_BOOTSTRAP_ADMIN_USERNAME=admin \
    KC_BOOTSTRAP_ADMIN_PASSWORD=password
# Esegui come user non-root
RUN adduser -D -u 1003 -g 1003 keycloak-user
# Eseguire la build prima dell'avvio per permettere l'uso di --optimized

USER keycloak-user
RUN /opt/keycloak/bin/kc.sh build

# Entrypoint per produzione con ottimizzazioni
ENTRYPOINT ["/opt/keycloak/bin/kc.sh","start-dev"]
