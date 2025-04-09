package com.howtodoinjava.app.applicationcore.service;

import com.howtodoinjava.app.applicationcore.entity.Utente;
import com.howtodoinjava.app.applicationcore.utility.KeycloakProperties;
import com.howtodoinjava.app.applicationcore.utility.KeycloakRoles;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    private UserResource getUserResource(String username){
        RealmResource realmResource = keycloak.realm(realm);
        System.out.println(username); // dev
        String userId = realmResource.users().search(username).get(0).getId();
        return realmResource.users().get(userId);
    }

    private ClientResource getClientResource(String clientId){
        RealmResource realmResource = keycloak.realm(realm);
        String id = realmResource.clients().findByClientId(clientId).get(0).getId();
        return realmResource.clients().get(id);
    }

    public void addRole(String username, KeycloakRoles role){
        UserResource userResource = getUserResource(username);
        ClientResource clientResource = getClientResource(appClientId);
        String id = clientResource.toRepresentation().getId();
        RoleRepresentation roleRepresentation = clientResource.roles().get(role.name()).toRepresentation();
        userResource.roles().clientLevel(id).add(Arrays.asList(roleRepresentation));
    }

    public void enableUser(String username, boolean enable){
        UserResource userResource = getUserResource(username);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setEnabled(enable);
        userResource.update(userRepresentation);
    }

    public List<?> getAllUsers(){
        List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().list();
        List<KCUser> users = new ArrayList<>();
        String idClient = getClientResource(appClientId).toRepresentation().getId();
        System.out.println(userRepresentations.get(0)); // dev
        for (UserRepresentation userRepresentation : userRepresentations) {
            Map<String,List<String>> userRoles = userRepresentation.getClientRoles();
            List<String> userClientRoles = new ArrayList<String>();
            if(userRoles != null) userClientRoles = userRoles.get(idClient);
            else userClientRoles.add("NONE");

            users.add( new KCUser(
                    userRepresentation.getUsername(),
                    userRepresentation.getEmail(),
                    userRepresentation.getAttributes().get("phone_number").get(0),
                    userClientRoles,
                    userRepresentation.isEnabled()
            ));
        }
        return users;
    }
}

class KCUser extends Utente {
    private final List<String> roles;
    private final boolean enabled;

    KCUser(
            String username,
            String email,
            String numCell,
            List<String> roles,
            boolean enabled
    ){
        super(username, email, numCell);
        this.roles = roles;
        this.enabled = enabled;
    }

    public List<String> getRoles() {
        return roles;
    }

    public boolean isEnabled() {
        return enabled;
    }
}