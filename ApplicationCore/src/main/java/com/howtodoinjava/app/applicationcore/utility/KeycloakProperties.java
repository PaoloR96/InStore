package com.howtodoinjava.app.applicationcore.utility;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("keycloak")
public record KeycloakProperties (
        String baseUrl
){}
