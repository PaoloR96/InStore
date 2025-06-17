package com.howtodoinjava.app.applicationcore.controller;

import com.howtodoinjava.app.applicationcore.dto.UtenteDTO;
import com.howtodoinjava.app.applicationcore.entity.Cliente;
import com.howtodoinjava.app.applicationcore.entity.Rivenditore;
import com.howtodoinjava.app.applicationcore.entity.Utente;
import com.howtodoinjava.app.applicationcore.service.AdminService;
import com.howtodoinjava.app.applicationcore.utility.JWTUtils;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/api")
@Validated
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/get-users")
    public ResponseEntity<List<UtenteDTO>> getUsers(Authentication auth) {
        List<UtenteDTO> users = adminService.getUsers();
        users.removeIf(u -> u.getUsername().equals(JWTUtils.getUsername(auth)));
        users.forEach(UtenteDTO::escape); // escaping
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/enable-user")
    public ResponseEntity<?> enableUser(
            @NotBlank(message = "L'username non può essere vuoto.")
            @Size(min = 3, max = 50, message = "L'username deve avere tra 3 e 50 caratteri.")
            @RequestParam String username) {
        try {
            adminService.enableUser(username, true);
            return ResponseEntity.ok().build();
        }catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Errore di validazione input: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/disable-user")
    public ResponseEntity<?> disableUser(
            @NotBlank(message = "L'username non può essere vuoto.")
            @Size(min = 3, max = 50, message = "L'username deve avere tra 3 e 50 caratteri.")
            @RequestParam String username) {
        try {
            adminService.enableUser(username, false);
            return ResponseEntity.ok().build();
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Errore di validazione input: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}