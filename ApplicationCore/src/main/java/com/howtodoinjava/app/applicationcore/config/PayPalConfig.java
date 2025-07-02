package com.howtodoinjava.app.applicationcore.config;

//import com.paypal.base.rest.APIContext;
//import com.paypal.base.rest.OAuthTokenCredential;
//import com.paypal.base.rest.PayPalRESTException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class PayPalConfig {
//
//    private static final Logger logger = LoggerFactory.getLogger(PayPalConfig.class);
//
//    @Value("${paypal.client.id}")
//    private String clientId;
//
//    @Value("${paypal.client.secret}")
//    private String clientSecret;
//
//    @Value("${paypal.mode:sandbox}")
//    private String mode;
//
//    @Bean
//    public Map<String, String> paypalSdkConfig() {
//        Map<String, String> configMap = new HashMap<>();
//        configMap.put("mode", mode);
//
//        // Configurazioni aggiuntive per il logging e timeout
//        configMap.put("http.ConnectionTimeOut", "5000");
//        configMap.put("http.Retry", "1");
//        configMap.put("http.ReadTimeOut", "30000");
//        configMap.put("http.MaxConnection", "100");
//        // Configurazioni per TLS sicuro
//        configMap.put("http.ssl.SSLEnabled", "true");
//        configMap.put("http.ssl.Protocol", "TLSv1.2");
//
//        logger.info("PayPal SDK configurato in modalità: {}", mode);
//        return configMap;
//    }
//
//    @Bean
//    public OAuthTokenCredential oAuthTokenCredential() {
//        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
//    }
//
//    @Bean
//    public APIContext apiContext() throws PayPalRESTException {
//        APIContext context = new APIContext(oAuthTokenCredential().getAccessToken());
//        context.setConfigurationMap(paypalSdkConfig());
//
//        logger.info("APIContext PayPal inizializzato correttamente");
//        return context;
//    }
//}
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class PayPalConfig {

    private static final Logger logger = LoggerFactory.getLogger(PayPalConfig.class);

    // Configurazioni di sicurezza avanzate
    private static final String ALLOWED_MODE_SANDBOX = "sandbox";
    private static final String ALLOWED_MODE_LIVE = "live";

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode:sandbox}")
    private String mode;

    // Configurazioni di timeout e retry personalizzabili
    @Value("${paypal.connection.timeout:5000}")
    private String connectionTimeout;

    @Value("${paypal.read.timeout:30000}")
    private String readTimeout;

    @Value("${paypal.max.connections:50}")
    private String maxConnections;

    @Value("${paypal.retry.attempts:2}")
    private String retryAttempts;

    // Cache thread-safe per le configurazioni
    private final Map<String, String> configCache = new ConcurrentHashMap<>();

    private void validateConfiguration() {
        logger.info("Inizializzazione configurazione PayPal...");

        // Validazione delle credenziali
        if (!isValidClientId(clientId)) {
            throw new IllegalArgumentException("Client ID PayPal non valido o vuoto");
        }

        if (!isValidClientSecret(clientSecret)) {
            throw new IllegalArgumentException("Client Secret PayPal non valido o vuoto");
        }

        // Validazione mode
        if (!isValidMode(mode)) {
            throw new IllegalArgumentException("PayPal mode deve essere 'sandbox' o 'live', ricevuto: " + mode);
        }

        // Log sicuro (senza esporre credenziali)
        logger.info("PayPal configurato in modalità: {}", mode);
        logger.info("Client ID presente: {}", !StringUtils.isEmpty(clientId));

        // Avviso di sicurezza per ambiente di produzione
        if (ALLOWED_MODE_LIVE.equals(mode)) {
            logger.warn("ATTENZIONE: PayPal configurato in modalità PRODUZIONE");
        }
    }

    @Bean
    public Map<String, String> paypalSdkConfig() {
        // Validazione lazy delle configurazioni
        validateConfiguration();

        if (configCache.isEmpty()) {
            synchronized (this) {
                if (configCache.isEmpty()) {
                    buildSecureConfiguration();
                }
            }
        }

        // Restituisce una copia immutabile per evitare modifiche esterne
        return new HashMap<>(configCache);
    }

    private void buildSecureConfiguration() {
        configCache.put("mode", mode);

        // Configurazioni di rete sicure
        configCache.put("http.ConnectionTimeOut", connectionTimeout);
        configCache.put("http.Retry", retryAttempts);
        configCache.put("http.ReadTimeOut", readTimeout);
        configCache.put("http.MaxConnection", maxConnections);

        // Configurazioni SSL/TLS avanzate
        configCache.put("http.ssl.SSLEnabled", "true");
        configCache.put("http.ssl.Protocol", "TLSv1.3"); // Versione più sicura
        configCache.put("http.ssl.TrustAllConnection", "false"); // Non fidati di tutti i certificati

        // Configurazioni di sicurezza aggiuntive
        configCache.put("http.UseProxy", "false");
        configCache.put("http.ProxySet", "false");

        // Headers di sicurezza
        configCache.put("http.UserAgent", "SecurePayPalClient/1.0");

        logger.debug("Configurazione PayPal SDK completata con {} parametri", configCache.size());
    }

    @Bean
    @Scope("singleton")
    public OAuthTokenCredential oAuthTokenCredential() {
        try {
            OAuthTokenCredential credential = new OAuthTokenCredential(
                    clientId,
                    clientSecret,
                    paypalSdkConfig()
            );

            logger.info("OAuthTokenCredential PayPal creato con successo");
            return credential;

        } catch (Exception e) {
            logger.error("Errore nella creazione delle credenziali PayPal", e);
            throw new RuntimeException("Impossibile inizializzare le credenziali PayPal", e);
        }
    }

    @Bean
    @Scope("prototype") // Nuovo bean per ogni richiesta per maggiore sicurezza
    public APIContext apiContext() throws PayPalRESTException {
        try {
            APIContext context = new APIContext(oAuthTokenCredential().getAccessToken());
            context.setConfigurationMap(paypalSdkConfig());

            // Impostazioni di sicurezza per il contesto
            context.setRequestId(generateSecureRequestId());

            logger.debug("APIContext PayPal inizializzato con successo");
            return context;

        } catch (PayPalRESTException e) {
            logger.error("Errore PayPal durante l'inizializzazione del contesto API: {}",
                    e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Errore generico durante l'inizializzazione del contesto PayPal", e);
            throw new PayPalRESTException("Inizializzazione contesto fallita", e);
        }
    }

    // Metodi di utilità per la validazione
    private boolean isValidClientId(String clientId) {
        return StringUtils.hasText(clientId) &&
                clientId.length() >= 50 && // Client ID PayPal tipicamente lungo
                clientId.matches("^[A-Za-z0-9._-]+$"); // Solo caratteri sicuri
    }

    private boolean isValidClientSecret(String clientSecret) {
        return StringUtils.hasText(clientSecret) &&
                clientSecret.length() >= 20 && // Secret tipicamente lungo
                !clientSecret.contains(" "); // Non deve contenere spazi
    }

    private boolean isValidMode(String mode) {
        return ALLOWED_MODE_SANDBOX.equals(mode) || ALLOWED_MODE_LIVE.equals(mode);
    }

    private String generateSecureRequestId() {
        return "REQ-" + System.currentTimeMillis() + "-" +
                Thread.currentThread().getId();
    }

    // Bean per monitoring e health check
    @Bean
    public PayPalHealthIndicator payPalHealthIndicator() {
        return new PayPalHealthIndicator(this);
    }

    // Classe interna per health check
    public static class PayPalHealthIndicator {
        private final PayPalConfig config;

        public PayPalHealthIndicator(PayPalConfig config) {
            this.config = config;
        }

        public boolean isHealthy() {
            try {
                // Test basico della configurazione
                return config.paypalSdkConfig() != null &&
                        config.oAuthTokenCredential() != null;
            } catch (Exception e) {
                logger.error("Health check PayPal fallito", e);
                return false;
            }
        }
    }
}