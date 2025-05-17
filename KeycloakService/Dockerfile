FROM keycloak/keycloak:26.2
LABEL authors="Gaetano"

ENV KC_BOOTSTRAP_ADMIN_USERNAME=admin \
    KC_BOOTSTRAP_ADMIN_PASSWORD=password

ENTRYPOINT ["/opt/keycloak/bin/kc.sh","start-dev"]
