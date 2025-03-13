package com.howtodoinjava.app.applicationcore.service;

import com.howtodoinjava.app.applicationcore.utility.KeycloakAdminCliProperties;
import com.howtodoinjava.app.applicationcore.utility.KeycloakProperties;
import org.keycloak.admin.client.Keycloak;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties({KeycloakProperties.class, KeycloakAdminCliProperties.class})
public class KeycloakService {
    private final Keycloak keycloak;

    public KeycloakService(KeycloakAdminCliProperties kcAdminCliProperties, KeycloakProperties kcProperties) {
        this.keycloak = Keycloak.getInstance(
                kcProperties.baseUrl(),
                kcAdminCliProperties.realm(),
                kcAdminCliProperties.username(),
                kcAdminCliProperties.password(),
                kcAdminCliProperties.clientId(),
                kcAdminCliProperties.clientSecret()
        );
    }


}
