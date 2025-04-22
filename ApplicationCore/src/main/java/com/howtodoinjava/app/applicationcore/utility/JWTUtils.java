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

    public static String getEmail(Authentication auth) throws RuntimeException {
        return getOidcUser(auth).getEmail();
    }

    public static String getPhoneNumber(Authentication auth) throws RuntimeException {
        OidcUser oidcUser = getOidcUser(auth);
        String phoneNumber = oidcUser.getPhoneNumber();
        if(phoneNumber != null) {
            return phoneNumber;
        }
        else throw new RuntimeException("Phone number is null");
    }

    private static OidcUser getOidcUser(Authentication auth) throws RuntimeException {
        if (auth instanceof OAuth2AuthenticationToken oauth && oauth.getPrincipal() instanceof OidcUser oidcUser) return oidcUser;
        else throw new RuntimeException("Invalid authentication token");
    }

    public static List<String> getAuthorities(Authentication auth){
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }
}
