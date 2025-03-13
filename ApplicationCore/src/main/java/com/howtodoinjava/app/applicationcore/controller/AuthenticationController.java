package com.howtodoinjava.app.applicationcore.controller;

import com.howtodoinjava.app.applicationcore.entity.Cliente;
import com.howtodoinjava.app.applicationcore.service.AuthenticationService;
import com.howtodoinjava.app.applicationcore.service.ClienteService;
import com.howtodoinjava.app.applicationcore.utility.JWTUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ClienteService clienteService;

    public AuthenticationController(AuthenticationService authenticationService, ClienteService clienteService) {
        this.authenticationService = authenticationService;
        this.clienteService = clienteService;
    }

    @GetMapping("/api/user-details")
    ResponseEntity<?> printUser(Authentication auth) {
        try{
            Account account = new Account(
                    JWTUtils.getUsername(auth),
                    JWTUtils.getAuthorities(auth)
            );
            System.out.println("default-controller:");
            System.out.println(JWTUtils.getOidcUser(auth).getClaims());
            return ResponseEntity.ok(account);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/api/login-redirect")
    ResponseEntity<?> loginRedirect(Authentication auth) {
        try{
            List<String> userRoles = JWTUtils.getAuthorities(auth);
            String redirectUrl = authenticationService.loginRedirect(userRoles);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, redirectUrl)
                    .build();
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/api/rivenditore-registration")
    ResponseEntity<?> rivenditoreRegistration(Authentication auth,
      @RequestParam String nomeSocieta,
      @RequestParam String partitaIva,
      @RequestParam String iban) {


        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/cliente-registration")
    ResponseEntity<?> clienteRegistration(Authentication auth,
        @RequestParam String nome,
        @RequestParam String cognome,
        @RequestParam String numeroCarta,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataScadenza,
        @RequestParam String nomeIntestatario,
        @RequestParam String cognomeIntestatario,
        @RequestParam String cvc) {
            try{
                String username = JWTUtils.getUsername(auth);
                String email = JWTUtils.getEmail(auth);
                String phoneNumber = JWTUtils.getPhoneNumber(auth);
                Cliente cliente = clienteService.creareClienteStandard(username, email, phoneNumber, nome, cognome, numeroCarta,
                        dataScadenza, nomeIntestatario, cognomeIntestatario, cvc);
                return ResponseEntity.created(URI.create("/api/user-details")).body(cliente);
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
    }
}
