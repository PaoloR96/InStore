FROM keycloak/keycloak:26.0.4
LABEL authors="Gaetano"

ENV KC_BOOTSTRAP_ADMIN_USERNAME=admin \
    KC_BOOTSTRAP_ADMIN_PASSWORD=password
# Esegui come user non-root

# Eseguire la build prima dell'avvio per permettere l'uso di --optimized
RUN /opt/keycloak/bin/kc.sh build

# Entrypoint per produzione con ottimizzazioni
ENTRYPOINT ["/opt/keycloak/bin/kc.sh","start-dev"]
