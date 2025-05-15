package com.howtodoinjava.app.applicationcore.controller;

import com.howtodoinjava.app.applicationcore.dto.ClienteDTO;
import com.howtodoinjava.app.applicationcore.dto.RivenditoreDTO;
import com.howtodoinjava.app.applicationcore.dto.UtenteDTO;
import com.howtodoinjava.app.applicationcore.entity.Cliente;
import com.howtodoinjava.app.applicationcore.entity.Rivenditore;
import com.howtodoinjava.app.applicationcore.service.AuthenticationService;
import com.howtodoinjava.app.applicationcore.service.ClienteService;
import com.howtodoinjava.app.applicationcore.utility.JWTUtils;
import jakarta.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.net.URI;
import java.util.*;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/user-details")
    ResponseEntity<?> printUser(Authentication auth) {
        try{
            UtenteDTO utenteDTO = new UtenteDTO(
                    JWTUtils.getUsername(auth),
                    JWTUtils.getEmail(auth),
                    JWTUtils.getPhoneNumber(auth),
                    JWTUtils.getAuthorities(auth),
                    true // TODO modify with the rigth value of the claim
            );
            return ResponseEntity.ok(utenteDTO);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping({ "/", "/index.html", "/index"})
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
    public ResponseEntity<?> rivenditoreRegistration(Authentication auth,
              @RequestParam String nomeSocieta,
              @RequestParam String partitaIva,
              @RequestParam String iban,
              HttpSession session) {
        try{
            String username = JWTUtils.getUsername(auth);
            String email = JWTUtils.getEmail(auth);
            String phoneNumber = JWTUtils.getPhoneNumber(auth);
            Rivenditore rivenditore = authenticationService.registerRivenditore(
                    username, email, phoneNumber, nomeSocieta, partitaIva, iban);
            session.invalidate();
            RivenditoreDTO rivenditoreDTO = new RivenditoreDTO(rivenditore);
            rivenditoreDTO.escape(); // escaping
            return ResponseEntity.status(HttpStatus.SEE_OTHER).location(URI.create("/rivenditore/index")).body(rivenditoreDTO);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/api/cliente-registration")
    public ResponseEntity<?> clienteRegistration(Authentication auth,
             @RequestParam String nome,
             @RequestParam String cognome,
             @RequestParam String numeroCarta,
             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataScadenza,
             @RequestParam String nomeIntestatario,
             @RequestParam String cognomeIntestatario,
             @RequestParam String cvc,
             HttpSession httpSession) {
        try{
            String username = JWTUtils.getUsername(auth);
            String email = JWTUtils.getEmail(auth);
            String phoneNumber = JWTUtils.getPhoneNumber(auth);
            Cliente cliente = authenticationService.registerCliente(username, email, phoneNumber, nome, cognome, numeroCarta,
                    dataScadenza, nomeIntestatario, cognomeIntestatario, cvc);
            httpSession.invalidate();
            ClienteDTO clienteDTO = new ClienteDTO(cliente);
            clienteDTO.escape();
            return ResponseEntity.status(HttpStatus.SEE_OTHER).location(URI.create("/cliente/index")).body(clienteDTO);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/csp-report")
    public ResponseEntity<?> cspReport(@RequestBody String requestBody){
        LoggerFactory.getLogger(AuthenticationController.class).info(requestBody);
        // escaping
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        Map<String, Object> report = jsonParser.parseMap(requestBody);
        for (Map.Entry<String, Object> entry : report.entrySet()) {
            entry.setValue(HtmlUtils.htmlEscape(entry.getValue().toString()));
        }
        return ResponseEntity.ok(requestBody);
    }
}
