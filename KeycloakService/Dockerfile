FROM keycloak/keycloak:26.3.0
LABEL authors="Gaetano"

ENV KC_BOOTSTRAP_ADMIN_USERNAME=admin \
    KC_BOOTSTRAP_ADMIN_PASSWORD=password
# Esegui come user non-root

RUN /opt/keycloak/bin/kc.sh build

# Entrypoint per produzione con ottimizzazioni
ENTRYPOINT ["/opt/keycloak/bin/kc.sh","start-dev"]
