package com.howtodoinjava.app.applicationcore.controller;

import com.howtodoinjava.app.applicationcore.utility.JWTUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DefaultController {

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

    @GetMapping("/api/check-role")
    ResponseEntity<?> checkRole(Authentication auth) {
        try{
            List<String> userRoles = JWTUtils.getAuthorities(auth);
            if(userRoles.isEmpty()){
                //TODO redirect to completeRegistration
                return ResponseEntity.status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, "/api/details")
                        .build();
            }
            else{
                String role = userRoles.get(0);
                return switch (role) {
                    case "CLIENTE" ->
                        //TODO
                            ResponseEntity.status(HttpStatus.FOUND)
                                    .header(HttpHeaders.LOCATION, "/api/cliente")
                                    .build();
                    case "ADMIN" ->
                        //TODO
                            ResponseEntity.status(HttpStatus.FOUND)
                                    .header(HttpHeaders.LOCATION, "/api/admin")
                                    .build();
                    case "RIVENDITORE" ->
                        //TODO
                            ResponseEntity.status(HttpStatus.FOUND)
                                    .header(HttpHeaders.LOCATION, "/api/rivenditore")
                                    .build();
                    default ->
                        //TODO
                            ResponseEntity.badRequest().body("Authentication error");
                };
            }
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
