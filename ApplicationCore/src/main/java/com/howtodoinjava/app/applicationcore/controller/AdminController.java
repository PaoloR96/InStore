package com.howtodoinjava.app.applicationcore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AdminController {

    @GetMapping("/api/admin")
    ResponseEntity<?> checkRole(Authentication auth) {
        if (auth instanceof OAuth2AuthenticationToken oauth && oauth.getPrincipal() instanceof OidcUser oidcUser) {
            Account account = new Account(
                    oidcUser.getPreferredUsername(),
                    auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
            );
            System.out.println(account);
            return ResponseEntity.ok(account);
        }
        else return ResponseEntity.badRequest().body("Errore di autenticazione");
    }

    @GetMapping("/api/check-role")
    ResponseEntity<?> index(Authentication auth) {
        return checkRole(auth);
    }
}

record Account(
    String name,
    List<String> roles
){}