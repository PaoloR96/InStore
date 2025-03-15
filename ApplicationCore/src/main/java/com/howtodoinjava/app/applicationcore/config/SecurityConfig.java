package com.howtodoinjava.app.applicationcore.config;

import com.howtodoinjava.app.applicationcore.utility.ApplicationProperties;
import com.howtodoinjava.app.applicationcore.utility.KeycloakProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SecurityConfig class configures security settings for the application,
 * enabling security filters and setting up OAuth2 login and logout behavior.
 */

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties({ApplicationProperties.class, KeycloakProperties.class})
public class SecurityConfig {

// TODO remove
// local hosts (only for testing)
//    private static final String kcHost = "localhost:8090";
//    private static final String appHost = "localhost:8080";
// remote hosts
//    private static final String kcHost = "keycloak-manager:8080";
//    private static final String appHost = "instore.puntoitstore.it";

    @Value("${spring.security.oauth2.client.registration.instore.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.instore.client-secret}")
    private String clientSecret;

    private static final String RESOURCE_ACCESS_CLAIM = "resource_access";
    private static final String CLIENT_ID_CLAIM = "instore-client";
    private static final String ROLES_CLAIM = "roles";

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(ApplicationProperties appProperties,
                                                                     KeycloakProperties kcProperties
    ) throws URISyntaxException {
        String kcBaseUrl = kcProperties.baseUrl();
        String appBaseUrl = appProperties.baseUrl();

//        return new InMemoryClientRegistrationRepository(this.keycloakClientRegistration());
        URI metadataEndpoint = new URI(kcBaseUrl + "/realms/instore/.well-known/openid-configuration");
        RequestEntity<Void> request = RequestEntity.get(metadataEndpoint).build();
        ParameterizedTypeReference<Map<String, Object>> typeReference = new ParameterizedTypeReference<>() {};
        Map<String, Object> configuration = new RestTemplate().exchange(request, typeReference).getBody();
        if(configuration == null) throw new RuntimeException("Configuration is null");
        ClientRegistration clientRegistration = ClientRegistrations.fromOidcConfiguration(configuration)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .registrationId("instore")
                .redirectUri(appBaseUrl + "/login/oauth2/code/instore")
                .scope("openid", "profile", "email")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .build();
        return new InMemoryClientRegistrationRepository(clientRegistration);
    }

    //TODO remove this method
//    private ClientRegistration keycloakClientRegistration() {
//        return ClientRegistration.withRegistrationId("instore")
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUri("http://"+appHost+"/login/oauth2/code/instore")
//                .scope("openid", "profile", "email")
//                .authorizationUri("http://"+kcHost+"/realms/instore/protocol/openid-connect/auth")
//                .tokenUri("http://"+kcHost+"/realms/instore/protocol/openid-connect/token")
//                .userInfoUri("http://"+kcHost+"/realms/instore/protocol/openid-connect/userinfo")
//                .userNameAttributeName(IdTokenClaimNames.SUB)
//                .jwkSetUri("http://"+kcHost+"/realms/instore/protocol/openid-connect/certs")
//                .clientName("instore-client")
//                .issuerUri("http://"+kcHost+"/realms/instore")
//                .build();
//    }

    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {
        return authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            var authority = authorities.iterator().next();
            boolean isOidc = authority instanceof OidcUserAuthority;

            if (isOidc) {
                var oidcUserAuthority = (OidcUserAuthority) authority;
                var userInfo = oidcUserAuthority.getIdToken();
                if (userInfo.hasClaim(RESOURCE_ACCESS_CLAIM)) {
                    var resourceAccess = userInfo.getClaimAsMap(RESOURCE_ACCESS_CLAIM);
                    if(resourceAccess.containsKey(CLIENT_ID_CLAIM)){
                        var clientResource = (Map<String,Object>) resourceAccess.get(CLIENT_ID_CLAIM);
                        if(clientResource.containsKey(ROLES_CLAIM)){
                            var roles = (Collection<String>) clientResource.get(ROLES_CLAIM);
                            mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                        }
                    }

                }
            } else {
                var oauth2UserAuthority = (OAuth2UserAuthority) authority;
                Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

                if (userAttributes.containsKey(RESOURCE_ACCESS_CLAIM)) {
                    var resourceAccess = (Map<String,Object>) userAttributes.get(RESOURCE_ACCESS_CLAIM);
                    if(resourceAccess.containsKey(CLIENT_ID_CLAIM)){
                        var clientAccess = (Map<String,Object>) resourceAccess.get(CLIENT_ID_CLAIM);
                        if (clientAccess.containsKey(ROLES_CLAIM)) {
                            var roles = (Collection<String>) clientAccess.get(ROLES_CLAIM);
                            mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                        }
                    }

                }
            }
            return mappedAuthorities;
        };
    }

    Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           ClientRegistrationRepository clientRegistrationRepository,
                                           ApplicationProperties appProperties
    ) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/clienti/**").hasAuthority("CLIENTE")
                        .requestMatchers("/api/rivenditori/**").hasAuthority("RIVENDITORE")
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                        .defaultSuccessUrl("/api/login-redirect",true)
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository, appProperties.baseUrl()))
                );
        return http.build();
    }


    private LogoutSuccessHandler oidcLogoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository, String appBaseUrl) {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);

//         Sets the location that the End-User's User Agent will be redirected to
//         after the logout has been performed at the Provider
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri(appBaseUrl + "/");

        return oidcLogoutSuccessHandler;
    }
}