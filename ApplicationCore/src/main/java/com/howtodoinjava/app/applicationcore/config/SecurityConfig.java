package com.howtodoinjava.app.applicationcore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
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

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .oauth2Login(withDefaults())
                .logout(logout -> logout
                        .logoutSuccessHandler(oidcLogoutSuccessHandler())
                );
        return http.build();
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);

        // Sets the location that the End-User's User Agent will be redirected to
        // after the logout has been performed at the Provider
//        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("http://localhost:8080/");

        return oidcLogoutSuccessHandler;
    }
}
