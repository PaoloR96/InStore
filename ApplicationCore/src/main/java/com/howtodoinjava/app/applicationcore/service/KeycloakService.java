package com.howtodoinjava.app.applicationcore.service;

import com.howtodoinjava.app.applicationcore.utility.KeycloakAdminCliProperties;
import com.howtodoinjava.app.applicationcore.utility.KeycloakProperties;
import com.howtodoinjava.app.applicationcore.utility.KeycloakRoles;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
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

    public void addRole(String username, KeycloakRoles role){
        UserResource user = keycloak.realm("instore").users().get(username);
        UserRepresentation userRepresentation = user.toRepresentation();
        System.out.println(userRepresentation.getClientRoles());
    }
}
