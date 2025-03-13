package com.howtodoinjava.app.applicationcore.utility;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("keycloak.admin-cli")
public record KeycloakAdminCliProperties (
        String realm,
        String username,
        String password,
        String clientId,
        String clientSecret
){}
