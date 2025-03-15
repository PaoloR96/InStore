package com.howtodoinjava.app.applicationcore.utility;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("application")
public record ApplicationProperties (
        String baseUrl
){}
