package com.howtodoinjava.app.applicationcore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * SecurityConfig class configures security settings for the application,
 * enabling security filters and setting up OAuth2 login and logout behavior.
 */

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

    @Value("${spring.security.oauth2.client.provider.instore.issuer-uri}")
    private String issuerUri;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.keycloakClientRegistration());
    }

    private ClientRegistration keycloakClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId("instore-client")
                .clientSecret("W1Kf5xedBWHaVgWrg3T85BjMyxttTXhR")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://applicationcore:8080/login/oauth2/code/instore")
                .scope("openid", "profile", "email")
                .authorizationUri("http://keycloak_manager:8080/realms/instore/protocol/openid-connect/auth")
                .tokenUri("http://keycloak_manager:8080/realms/instore/protocol/openid-connect/token")
                .userInfoUri("http://keycloak_manager:8080/realms/instore/protocol/openid-connect/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("http://keycloak_manager:8080/realms/instore/protocol/openid-connect/certs")
                .clientName("instore-client")
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .oauth2Login(withDefaults())
                .logout(logout -> logout
                        .logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository))
                );
        return http.build();
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository) {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);

        // Sets the location that the End-User's User Agent will be redirected to
        // after the logout has been performed at the Provider
//        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("http://localhost:8080/");

        return oidcLogoutSuccessHandler;
    }
}
