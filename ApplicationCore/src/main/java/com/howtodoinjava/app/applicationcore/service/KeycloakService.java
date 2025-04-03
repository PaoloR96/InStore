package com.howtodoinjava.app.applicationcore.service;

import com.howtodoinjava.app.applicationcore.utility.KeycloakAdminCliProperties;
import com.howtodoinjava.app.applicationcore.utility.KeycloakProperties;
import com.howtodoinjava.app.applicationcore.utility.KeycloakRoles;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
@EnableConfigurationProperties({KeycloakProperties.class, KeycloakAdminCliProperties.class})
public class KeycloakService {
    private final Keycloak keycloak;
    private final static String realm = "instore";
    private final static String appClient = "instore-client";
//    private final static String masterRealm = "instore";
    private final static String adminClientId = "admin-cli";
//    private final static String username = "service-account-admin-cli";
//    private final static String password = "service-account-admin-cli";
    private final static String adminClientSecret = "fZHTYsp7StlgjwbYRSmEYfcWB5ipAAZS";


    public KeycloakService(KeycloakAdminCliProperties kcAdminCliProperties, KeycloakProperties kcProperties) {
        this.keycloak = KeycloakBuilder.builder()
            .serverUrl(kcProperties.baseUrl())
            .realm(realm)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .clientId(adminClientId)
            .clientSecret(adminClientSecret)
            .build();
    }

    public void addRole(String username, KeycloakRoles role){
        RealmResource realmResource = keycloak.realm(realm);
        String userId = realmResource.users().search(username).get(0).getId();
        UserResource userResource = realmResource.users().get(userId);
        String id = realmResource.clients().findByClientId(appClient).get(0).getId();
        ClientResource clientResource = realmResource.clients().get(id);
        RoleRepresentation roleRepresentation = clientResource.roles().get(role.name()).toRepresentation();
        userResource.roles().clientLevel(id).add(Arrays.asList(roleRepresentation));
    }
}
