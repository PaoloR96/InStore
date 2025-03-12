package com.howtodoinjava.app.applicationcore.controller;

import com.howtodoinjava.app.applicationcore.utility.JWTUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DefaultController {

    @GetMapping("/api/print-user")
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
            }
            else{
                String role = userRoles.get(0);
                switch (role) {
                    case "CLIENTE":
                        //TODO
                        break;

                    case "ADMIN":
                        //TODO
                        break;

                    case "RIVENDITORE":
                        //TODO
                        break;

                    default:
                        //TODO
                }
            }
            return ResponseEntity.ok().build();

        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
