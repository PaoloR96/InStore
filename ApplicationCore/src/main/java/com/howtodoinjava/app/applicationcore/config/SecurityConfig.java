package com.howtodoinjava.app.applicationcore.config;

import com.howtodoinjava.app.applicationcore.utility.ApplicationProperties;
import com.howtodoinjava.app.applicationcore.utility.KeycloakProperties;
import com.howtodoinjava.app.applicationcore.utility.KeycloakRoles;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
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
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SecurityConfig class configures security settings for the application,
 * enabling security filters and setting up OAuth2 login and logout behavior.
 */

//interface AuthoritiesConverter extends Converter<Map<String, Object>, Collection<GrantedAuthority>> {}

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties({ApplicationProperties.class, KeycloakProperties.class, OAuth2ClientProperties.class})
public class SecurityConfig {

    private static String realm;
    private static String kcBaseUrl;
    private static String appBaseUrl;
    private static String clientId;
    private static String clientSecret;
    private static String cspReportEndpoint;

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
        cspReportEndpoint = appBaseUrl + "/csp-report";
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
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

//    @Bean
//    AuthoritiesConverter realmRolesAuthoritiesConverter() {
//        return claims -> {
//            var realmAccess = Optional.ofNullable((Map<String, Object>) claims.get(RESOURCE_ACCESS_CLAIM));
//            var roles = realmAccess.flatMap(map -> Optional.ofNullable((List<String>) map.get(ROLES_CLAIM)));
//            return roles.map(List::stream)
//                    .orElse(Stream.empty())
//                    .map(SimpleGrantedAuthority::new)
//                    .map(GrantedAuthority.class::cast)
//                    .toList();
//        };
//    }
//
//    @Bean
//    GrantedAuthoritiesMapper authenticationConverter(
//            AuthoritiesConverter authoritiesConverter) {
//        return (authorities) -> authorities.stream()
//                .filter(authority -> authority instanceof OidcUserAuthority)
//                .map(OidcUserAuthority.class::cast)
//                .map(OidcUserAuthority::getIdToken)
//                .map(OidcIdToken::getClaims)
//                .map(authoritiesConverter::convert).filter(Objects::nonNull)
//                .flatMap(Collection::stream)
//                .collect(Collectors.toSet());
//    }

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
                    .requestMatchers("/complete-registration.html").not().hasAnyAuthority(
                            KeycloakRoles.CLIENTE.name(),
                            KeycloakRoles.RIVENDITORE.name(),
                            KeycloakRoles.ADMIN.name()
                    )
                    .anyRequest().authenticated()
            )
            .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                    .defaultSuccessUrl("/",true)
            )
            .logout(logout -> logout
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository, appProperties.baseUrl()))
            )
            .sessionManagement(
                    sessionManagementConfigurer -> sessionManagementConfigurer
                            .sessionAuthenticationStrategy(new RegisterSessionAuthenticationStrategy(sessionRegistry()))
                            .sessionConcurrency(session -> session.maximumSessions(1))
            )
            .csrf(csrfConfigurer -> csrfConfigurer
                    .ignoringRequestMatchers("/csp-report")
            )
            .headers(headers -> headers
                    .contentSecurityPolicy(contentSecurityPolicyConfig -> contentSecurityPolicyConfig
                            .policyDirectives(getContentSecurityPolicy())
//                            .reportOnly()
                    )
                    .addHeaderWriter(new StaticHeadersWriter(
                            "Reporting-Endpoints",
                            "csp-endpoint=\"" + cspReportEndpoint + "\""))
            );

        return http.build();
    }

    private String getContentSecurityPolicy() {
        String styleSrc = "; style-src 'self' https://cdnjs.cloudflare.com https://fonts.googleapis.com https://fonts.gstatic.com";
        String reportTo = "; report-to csp-endpoint; report-uri /csp-report";
        String scriptSrc = "; script-src 'self' https://code.jquery.com https://cdn.jsdelivr.net";
        String fontSrc = "; font-src 'self' https://cdnjs.cloudflare.com https://fonts.googleapis.com https://fonts.gstatic.com";

        return "default-src 'self'" + scriptSrc + styleSrc + fontSrc + reportTo + "; upgrade-insecure-requests";
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