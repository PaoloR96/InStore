package com.howtodoinjava.app.applicationcore.service;

import com.howtodoinjava.app.applicationcore.entity.Cliente;
import com.howtodoinjava.app.applicationcore.entity.Rivenditore;
import com.howtodoinjava.app.applicationcore.entity.StatoRivenditore;
import com.howtodoinjava.app.applicationcore.utility.JWTUtils;
import com.howtodoinjava.app.applicationcore.utility.KeycloakRoles;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.endpoint.RestClientRefreshTokenTokenResponseClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AuthenticationService {

    private final ClienteService clienteService;
    private final KeycloakService keycloakService;
    private final RivenditoreService rivenditoreService;

    public AuthenticationService(
            ClienteService clienteService,
            KeycloakService keycloakService,
            RivenditoreService rivenditoreService
    ) {
        this.clienteService = clienteService;
        this.keycloakService = keycloakService;
        this.rivenditoreService = rivenditoreService;
    }

    public String loginRedirect(List<String> userRoles) throws RuntimeException{
        if(userRoles.isEmpty()){
            //TODO redirect to completeRegistration
            return "/complete-registration.html";
        }
        else{
            KeycloakRoles role = KeycloakRoles.valueOf(userRoles.get(0));
            return switch (role) {
                case CLIENTE, CLIENTE_PREMIUM -> "/cliente/index.html";

                case ADMIN -> "/admin/index.html";

                case RIVENDITORE -> "/rivenditore/index.html";

                default -> throw new RuntimeException("Authentication Error");
            };
        }
    }

    public Cliente registerCliente(
        String username, String email, String numCell,
        String nome, String cognome, String numeroCarta, Date dataScadenza,
        String nomeIntestatario, String cognomeIntestatario, String cvc
    ){
        keycloakService.addRole(username, KeycloakRoles.CLIENTE);
        return clienteService.creareClienteStandard(
                username, email, numCell, nome, cognome, numeroCarta, dataScadenza, nomeIntestatario, cognomeIntestatario, cvc);
    }

    public Rivenditore registerRivenditore(
            String username,
            String email,
            String phoneNumber,
            String nomeSocieta,
            String partitaIva,
            String iban
    ){
        keycloakService.addRole(username, KeycloakRoles.RIVENDITORE);
        return rivenditoreService.createRivenditore(
                email,
                username,
                phoneNumber,
                nomeSocieta,
                partitaIva,
                iban
        );
    }


}
