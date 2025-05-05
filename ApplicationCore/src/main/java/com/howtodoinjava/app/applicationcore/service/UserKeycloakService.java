package com.howtodoinjava.app.applicationcore.service;

import com.howtodoinjava.app.applicationcore.utility.KeycloakProperties;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties({KeycloakProperties.class, OAuth2ClientProperties.class})
public class UserKeycloakService {
    private final Keycloak keycloak;
    private final String realm;

    public UserKeycloakService(
            KeycloakProperties kcProperties,
            OAuth2ClientProperties oAuth2ClientProperties)
    {
        this.realm = kcProperties.realm();
        OAuth2ClientProperties.Registration oAuth2ClientRegistration = oAuth2ClientProperties.getRegistration().get(this.realm);
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(kcProperties.baseUrl())
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(oAuth2ClientRegistration.getClientId())
                .clientSecret(oAuth2ClientRegistration.getClientSecret())
                .build();
    }


}
