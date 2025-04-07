package com.howtodoinjava.app.applicationcore.service;

import com.howtodoinjava.app.applicationcore.utility.KeycloakProperties;
import com.howtodoinjava.app.applicationcore.utility.KeycloakRoles;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@EnableConfigurationProperties({KeycloakProperties.class, OAuth2ClientProperties.class})
public class KeycloakService {

    private final Keycloak keycloak;
    private final String realm;
    private final String appClientId;

    public KeycloakService(KeycloakProperties kcProperties, OAuth2ClientProperties oAuth2ClientProperties) {
        this.realm = kcProperties.realm();
        this.appClientId = oAuth2ClientProperties.getRegistration().get(this.realm).getClientId();
        this.keycloak = KeycloakBuilder.builder()
            .serverUrl(kcProperties.baseUrl())
            .realm(realm)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .clientId(kcProperties.adminClientId())
            .clientSecret(kcProperties.adminClientSecret())
            .build();
    }

    public void addRole(String username, KeycloakRoles role){
        RealmResource realmResource = keycloak.realm(realm);
        String userId = realmResource.users().search(username).get(0).getId();
        UserResource userResource = realmResource.users().get(userId);
        String id = realmResource.clients().findByClientId(appClientId).get(0).getId();
        ClientResource clientResource = realmResource.clients().get(id);
        RoleRepresentation roleRepresentation = clientResource.roles().get(role.name()).toRepresentation();
        userResource.roles().clientLevel(id).add(Arrays.asList(roleRepresentation));
    }
}
