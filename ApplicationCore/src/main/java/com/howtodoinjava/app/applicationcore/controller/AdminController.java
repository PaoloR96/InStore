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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("")
    ResponseEntity<?> index(Authentication auth) {
        System.out.println("********DEBUG********");
        System.out.println(auth.getAuthorities().stream().toList());
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
}

record Account(
    String name,
    List<String> roles
){}