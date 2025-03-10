package com.howtodoinjava.app.applicationcore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/")
    ResponseEntity<?> adminConsole(Authentication auth) {
        if (auth instanceof OAuth2AuthenticationToken oauth && oauth.getPrincipal() instanceof OidcUser oidcUser) {
            Account account = new Account(
                    oidcUser.getPreferredUsername(),
                    auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
            );
            return ResponseEntity.ok(account);
        }
        else return ResponseEntity.badRequest().body("Errore di autenticazione");
    }
}

record Account(
    String name,
    List<String> roles
){}