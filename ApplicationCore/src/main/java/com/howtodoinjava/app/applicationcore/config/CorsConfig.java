package com.howtodoinjava.app.applicationcore.config;

import com.howtodoinjava.app.applicationcore.utility.ApplicationProperties;
import com.howtodoinjava.app.applicationcore.utility.KeycloakProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//TODO serve?
@Configuration
@EnableConfigurationProperties({ApplicationProperties.class, KeycloakProperties.class})
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer(ApplicationProperties appProperties) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("https://localhost:8081", appProperties.baseUrl()) // Frontend servito dallo stesso server
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowCredentials(true);
                registry.addMapping("/cliente/api/**")
                        .allowedOrigins("https://localhost:8081", appProperties.baseUrl()) // Frontend servito dallo stesso server
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowCredentials(true);
                registry.addMapping("/rivenditore/api/**")
                        .allowedOrigins("https://localhost:8081", appProperties.baseUrl()) // Frontend servito dallo stesso server
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowCredentials(true);
                registry.addMapping("/admin/api/**")
                        .allowedOrigins("https://localhost:8081", appProperties.baseUrl()) // Frontend servito dallo stesso server
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowCredentials(true);
            }
        };
    }
}
