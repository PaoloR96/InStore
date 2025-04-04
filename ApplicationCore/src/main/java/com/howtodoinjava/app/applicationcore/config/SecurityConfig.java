package com.howtodoinjava.app.applicationcore.config;

import com.howtodoinjava.app.applicationcore.utility.ApplicationProperties;
import com.howtodoinjava.app.applicationcore.utility.KeycloakProperties;
import com.howtodoinjava.app.applicationcore.utility.KeycloakRoles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
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
@EnableConfigurationProperties({ApplicationProperties.class, KeycloakProperties.class, OAuth2ClientProperties.class})
public class SecurityConfig {

//    @Value("${spring.security.oauth2.client.registration.instore.client-id}")
//    private static String clientId;
//    @Value("${spring.security.oauth2.client.registration.instore.client-secret}")
//    private static String clientSecret;
    private static String realm;
    private static String kcBaseUrl;
    private static String appBaseUrl;
    private static String clientId;
    private static String clientSecret;

    private static final String RESOURCE_ACCESS_CLAIM = "resource_access";
    private static final String ROLES_CLAIM = "roles";

    public SecurityConfig(
            ApplicationProperties appProperties,
            KeycloakProperties kcProperties,
            OAuth2ClientProperties oAuth2ClientProperties
    ){
        realm = kcProperties.realm();
        kcBaseUrl = kcProperties.baseUrl();
        appBaseUrl = appProperties.baseUrl();
        clientId = oAuth2ClientProperties.getRegistration().get(realm).getClientId();
        clientSecret = oAuth2ClientProperties.getRegistration().get(realm).getClientSecret();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() throws URISyntaxException {
        URI metadataEndpoint = new URI(kcBaseUrl + "/realms/" + realm + "/.well-known/openid-configuration");
        RequestEntity<Void> request = RequestEntity.get(metadataEndpoint).build();
        ParameterizedTypeReference<Map<String, Object>> typeReference = new ParameterizedTypeReference<>() {};
        Map<String, Object> configuration = new RestTemplate().exchange(request, typeReference).getBody();
        if(configuration == null) throw new RuntimeException("Configuration is null");
        ClientRegistration clientRegistration = ClientRegistrations.fromOidcConfiguration(configuration)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .registrationId(realm)
                .redirectUri(appBaseUrl + "/login/oauth2/code/instore")
                .scope("openid", "profile", "email")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .build();
        return new InMemoryClientRegistrationRepository(clientRegistration);
    }

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
                    if(resourceAccess.containsKey(clientId)){
                        var clientResource = (Map<String,Object>) resourceAccess.get(clientId);
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
                    if(resourceAccess.containsKey(clientId)){
                        var clientAccess = (Map<String,Object>) resourceAccess.get(clientId);
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
                        .requestMatchers("/cliente/**").hasAuthority(KeycloakRoles.CLIENTE.name())
                        .requestMatchers("/rivenditore/**").hasAuthority(KeycloakRoles.RIVENDITORE.name())
                        .requestMatchers("/admin/**").hasAuthority(KeycloakRoles.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .csrf().disable()
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

        //TODO setting up a logout page
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri(appBaseUrl + "/");

        return oidcLogoutSuccessHandler;
    }
}