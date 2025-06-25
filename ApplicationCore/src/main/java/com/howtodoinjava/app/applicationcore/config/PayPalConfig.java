package com.howtodoinjava.app.applicationcore.config;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PayPalConfig {

    private static final Logger logger = LoggerFactory.getLogger(PayPalConfig.class);

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode:sandbox}")
    private String mode;

    @Bean
    public Map<String, String> paypalSdkConfig() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", mode);

        // Configurazioni aggiuntive per il logging e timeout
        configMap.put("http.ConnectionTimeOut", "5000");
        configMap.put("http.Retry", "1");
        configMap.put("http.ReadTimeOut", "30000");
        configMap.put("http.MaxConnection", "100");
        // Configurazioni per TLS sicuro
        configMap.put("http.ssl.SSLEnabled", "true");
        configMap.put("http.ssl.Protocol", "TLSv1.2");

        logger.info("PayPal SDK configurato in modalità: {}", mode);
        return configMap;
    }

    @Bean
    public OAuthTokenCredential oAuthTokenCredential() {
        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        APIContext context = new APIContext(oAuthTokenCredential().getAccessToken());
        context.setConfigurationMap(paypalSdkConfig());

        logger.info("APIContext PayPal inizializzato correttamente");
        return context;
    }
}