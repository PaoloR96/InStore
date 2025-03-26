package com.howtodoinjava.app.applicationcore.service;

import com.howtodoinjava.app.applicationcore.entity.Cliente;
import com.howtodoinjava.app.applicationcore.utility.KeycloakRoles;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AuthenticationService {

    private final ClienteService clienteService;
    private final KeycloakService keycloakService;

    public AuthenticationService(ClienteService clienteService, KeycloakService keycloakService) {
        this.clienteService = clienteService;
        this.keycloakService = keycloakService;
    }

    public String loginRedirect(List<String> userRoles) throws RuntimeException{
        if(userRoles.isEmpty()){
            //TODO redirect to completeRegistration
            return "/api/user-details";
        }
        else{
            KeycloakRoles role = KeycloakRoles.valueOf(userRoles.get(0));
            return switch (role) {
                case CLIENTE, CLIENTE_PREMIUM -> "/api/clienti";

                case ADMIN -> "/api/admin";

                case RIVENDITORE -> "/api/rivenditori";

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
        Cliente cliente = clienteService.creareClienteStandard(
                username, email, numCell, nome, cognome, numeroCarta, dataScadenza, nomeIntestatario, cognomeIntestatario, cvc);
        return cliente;
    }


}
