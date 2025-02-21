package com.howtodoinjava.app.applicationcore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * SecurityConfig class configures security settings for the application,
 * enabling security filters and setting up OAuth2 login and logout behavior.
 */

//TODO remove this class
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    /**
//     * Configures the security filter chain for handling HTTP requests, OAuth2 login, and logout.
//     *
//     * @param http HttpSecurity object to define web-based security at the HTTP level
//     * @return SecurityFilterChain for filtering and securing HTTP requests
//     * @throws Exception in case of an error during configuration
//     */
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                // Configures authorization rules for different endpoints
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/").permitAll() // Allows public access to the root URL
//                        .requestMatchers("/api/prodotti").authenticated() // Requires authentication to access "/menu"
//                        .anyRequest().authenticated() // Requires authentication for any other request
//                )
//                // Configures OAuth2 login settings
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/oauth2/authorization/keycloak") // Sets custom login page for OAuth2 with Keycloak
//                        .defaultSuccessUrl("/api/prodotti", true) // Redirects to "/menu" after successful login
//                )
//                // Configures logout settings
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/") // Redirects to the root URL on successful logout
//                        .invalidateHttpSession(true) // Invalidates session to clear session data
//                        .clearAuthentication(true) // Clears authentication details
//                        .deleteCookies("JSESSIONID") // Deletes the session cookie
//                );
//
//        return http.build();
//    }
//}

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //local hosts (only for testing)
//    final private String kcHost = "localhost:8090";
//    final private String appHost = "localhost:8080";

    //remote hosts
    final private String kcHost = "keycloak-manager:8080";
    final private String appHost = "applicationcore:8080";

    @Value("${spring.security.oauth2.client.registration.instore.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.instore.client-secret}")
    private String clientSecret;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() throws URISyntaxException {
//        return new InMemoryClientRegistrationRepository(this.keycloakClientRegistration());
        URI metadataEndpoint = new URI("http://"+kcHost+"/realms/instore/.well-known/openid-configuration");
        RequestEntity<Void> request = RequestEntity.get(metadataEndpoint).build();
        ParameterizedTypeReference<Map<String, Object>> typeReference = new ParameterizedTypeReference<>() {};
        Map<String, Object> configuration = new RestTemplate().exchange(request, typeReference).getBody();
        if(configuration == null) throw new RuntimeException("Configuration is null - Cazzo");
        System.out.println(configuration);
        ClientRegistration clientRegistration = ClientRegistrations.fromOidcConfiguration(configuration)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .registrationId("instore")
                .redirectUri("http://"+appHost+"/login/oauth2/code/instore")
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
    public SecurityFilterChain filterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .oauth2Login(withDefaults())
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository))
                );
        return http.build();
    }


    private LogoutSuccessHandler oidcLogoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository) {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);

//         Sets the location that the End-User's User Agent will be redirected to
//         after the logout has been performed at the Provider
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("http://"+appHost+":8080/");

        return oidcLogoutSuccessHandler;
    }
}