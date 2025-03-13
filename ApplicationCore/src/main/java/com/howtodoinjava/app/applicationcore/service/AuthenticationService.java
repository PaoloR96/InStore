package com.howtodoinjava.app.applicationcore.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    public String loginRedirect(List<String> userRoles) throws RuntimeException{
        if(userRoles.isEmpty()){
            //TODO redirect to completeRegistration
            return "/api/user-details";
        }
        else{
            String role = userRoles.get(0);
            return switch (role) {
                case "CLIENTE" -> "/api/user-details";

                case "ADMIN" -> "/api/admin";

                case "RIVENDITORE" -> "/api/rivenditore";

                default -> throw new RuntimeException("Authentication Error");
            };
        }
    }


}
