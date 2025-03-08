package com.howtodoinjava.app.applicationcore.utility;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;

public class JWTUtils {

    public static String getUsername(Authentication auth) throws RuntimeException {
        return getOidcUser(auth).getPreferredUsername();
    }

    public static OidcUser getOidcUser(Authentication auth) throws RuntimeException {
        if (auth instanceof OAuth2AuthenticationToken oauth && oauth.getPrincipal() instanceof OidcUser oidcUser) return oidcUser;
        else throw new RuntimeException("Invalid authentication token");
    }

    public static List<String> getAuthorities(Authentication auth) throws RuntimeException {
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }
}
