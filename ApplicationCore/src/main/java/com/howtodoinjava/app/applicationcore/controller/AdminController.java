package com.howtodoinjava.app.applicationcore.controller;

import com.howtodoinjava.app.applicationcore.dto.UtenteDTO;
import com.howtodoinjava.app.applicationcore.entity.Cliente;
import com.howtodoinjava.app.applicationcore.entity.Rivenditore;
import com.howtodoinjava.app.applicationcore.entity.Utente;
import com.howtodoinjava.app.applicationcore.service.AdminService;
import com.howtodoinjava.app.applicationcore.utility.JWTUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/api")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/get-users")
    public ResponseEntity<List<UtenteDTO>> getUsers(Authentication auth) {
        List<UtenteDTO> users = adminService.getUsers();
        users.removeIf(u -> u.getUsername().equals(JWTUtils.getUsername(auth)));
        return ResponseEntity.ok(users);
    }

    @GetMapping("/enable-user")
    public ResponseEntity<?> enableUser(@RequestParam String username) {
        adminService.enableUser(username, true);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/disable-user")
    public ResponseEntity<?> disableUser(@RequestParam String username) {
        adminService.enableUser(username, false);
        return ResponseEntity.ok().build();
    }
}