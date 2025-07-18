package com.howtodoinjava.app.applicationcore.service;

import com.howtodoinjava.app.applicationcore.dto.UtenteDTO;
import com.howtodoinjava.app.applicationcore.entity.Utente;
import com.howtodoinjava.app.applicationcore.utility.KeycloakProperties;
import com.howtodoinjava.app.applicationcore.utility.KeycloakRoles;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Configuration;
import org.hibernate.validator.internal.constraintvalidators.hv.CodePointLengthValidator;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.protocol.oidc.representations.OIDCConfigurationRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
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

    public KeycloakService(
            KeycloakProperties kcProperties,
            OAuth2ClientProperties oAuth2ClientProperties)
    {
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

    public List<UtenteDTO> getAllUsers(){
        List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().list();
        List<UtenteDTO> users = new ArrayList<>();
        String idClient = getClientResource(appClientId).toRepresentation().getId();
        for (UserRepresentation userRepresentation : userRepresentations) {
            Map<String,List<String>> userClientRoles = userRepresentation.getClientRoles();
            List<String> userRoles = new ArrayList<String>();
            if(userClientRoles != null) userRoles = userClientRoles.get(idClient);
            else{
                if(userRepresentation.getUsername().startsWith("u")) userRoles.add("CLIENTE");
                else if(userRepresentation.getUsername().startsWith("p")){
                    userRoles.add("CLIENTE");
                    userRoles.add("CLIENTE_PREMIUM");
                } else if(userRepresentation.getUsername().startsWith("a")) userRoles.add("ADMIN");
                else if(userRepresentation.getUsername().startsWith("s")) userRoles.add("RIVENDITORE");
                else userRoles.add("CLIENTE");
            }

            users.add( new UtenteDTO(
                    userRepresentation.getUsername(),
                    userRepresentation.getEmail(),
                    userRepresentation.getAttributes().get("phone_number").get(0),
                    userRoles,
                    userRepresentation.isEnabled()
            ));
        }
        return users;
    }
}