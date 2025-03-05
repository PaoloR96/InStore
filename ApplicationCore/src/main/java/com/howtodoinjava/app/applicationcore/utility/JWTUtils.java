package com.howtodoinjava.app.applicationcore.utility;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class JWTUtils {
    public static String getUsername(Authentication auth) throws RuntimeException {
        if (auth instanceof OAuth2AuthenticationToken oauth && oauth.getPrincipal() instanceof OidcUser oidcUser) {
            return oidcUser.getPreferredUsername();
        }
        else throw new RuntimeException("Invalid authentication token");
    }
}
