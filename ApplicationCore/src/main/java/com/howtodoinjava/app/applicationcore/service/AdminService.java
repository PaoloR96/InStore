package com.howtodoinjava.app.applicationcore.service;

import com.howtodoinjava.app.applicationcore.entity.Cliente;
import com.howtodoinjava.app.applicationcore.entity.Rivenditore;
import com.howtodoinjava.app.applicationcore.entity.Utente;
import com.howtodoinjava.app.applicationcore.repository.ClienteRepository;
import com.howtodoinjava.app.applicationcore.repository.RivenditoreRepository;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AdminService {

    private final ClienteRepository clienteRepository;
    private final RivenditoreRepository rivenditoreRepository;
    private final KeycloakService keycloakService;

    public AdminService(
            ClienteRepository clienteRepository,
            RivenditoreRepository rivenditoreRepository,
            KeycloakService keycloakService
    ) {
        this.clienteRepository = clienteRepository;
        this.rivenditoreRepository = rivenditoreRepository;
        this.keycloakService = keycloakService;
    }

    public List<?> getUsers(){
        return keycloakService.getAllUsers();
    }

    public List<Cliente> getClienti(){
        return clienteRepository.findAll();
    }

    public List<Rivenditore> getRivenditori(){
        return rivenditoreRepository.findAll();
    }

    public void enableUser(String username, boolean enable){
        keycloakService.enableUser(username, enable);
        // TODO remove
//        Optional<? extends Utente> user = clienteRepository.findById(username);
//        if(user.isEmpty()){
//            user = rivenditoreRepository.findById(username);
//        }
//        try{
//            user.get().setEnabled(enable);
//        } catch (NoSuchElementException e){
//            System.out.println(e.getMessage());
//        }
    }
}
