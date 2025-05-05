package com.howtodoinjava.app.applicationcore.service;

import com.howtodoinjava.app.applicationcore.dto.UtenteDTO;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final KeycloakService keycloakService;
    private final SessionRegistry sessionRegistry;

    public AdminService(KeycloakService keycloakService, SessionRegistry sessionRegistry) {
        this.keycloakService = keycloakService;
        this.sessionRegistry = sessionRegistry;
    }

    public List<UtenteDTO> getUsers() {
        return keycloakService.getAllUsers();
    }

    public void enableUser(String username, boolean enable){
        keycloakService.enableUser(username, enable);
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            if (principal instanceof OidcUser oidcUser) {
                if (oidcUser.getPreferredUsername().equals(username)){
                    sessionRegistry.getAllSessions(principal, false).forEach(SessionInformation::expireNow);
                }
            }
        }
    }
}
