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
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.net.URI;
import java.util.*;

@RestController
@Validated
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
            //TODO aggiungere escape utente ??
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
         @NotBlank(message = "Il nome della società non può essere vuoto.")
         @Size(max = 50, message = "Il nome della società non può superare i 50 caratteri.")
         @RequestParam String nomeSocieta,
         @NotBlank(message = "La Partita IVA non può essere vuota.")
         @Pattern(regexp = "^[0-9]{11}$", message = "La Partita IVA deve essere di 11 cifre numeriche.")
         @RequestParam String partitaIva,
         @NotBlank(message = "L'IBAN non può essere vuoto.")
         @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{4,30}$", message = "L'IBAN non è in un formato valido.")
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
         @NotBlank(message = "Il nome non può essere vuoto.")
         @Size(max = 50, message = "Il nome non può superare i 50 caratteri.")
         @RequestParam String nome,
         @NotBlank(message = "Il cognome non può essere vuoto.")
         @Size(max = 50, message = "Il cognome non può superare i 50 caratteri.")
         @RequestParam String cognome,
         @NotBlank(message = "Il numero della carta non può essere vuoto.")
         @CreditCardNumber(message = "Il numero della carta di credito non è valido.")
         @RequestParam String numeroCarta,
         @NotNull(message = "La data di scadenza non può essere nulla.")
         @Future(message = "La data di scadenza deve essere nel futuro.")
         @DateTimeFormat(pattern = "yyyy-MM-dd")
         @RequestParam Date dataScadenza,
         @NotBlank(message = "Il nome dell'intestatario non può essere vuoto.")
         @Size(max = 100, message = "Il nome dell'intestatario non può superare i 100 caratteri.")
         @RequestParam String nomeIntestatario,
         @NotBlank(message = "Il cognome dell'intestatario non può essere vuoto.")
         @Size(max = 100, message = "Il cognome dell'intestatario non può superare i 100 caratteri.")
         @RequestParam String cognomeIntestatario,
         @NotBlank(message = "Il CVC non può essere vuoto.")
         @Size(min = 3, max = 3, message = "Il CVC deve avere 3 cifre.")
         @Pattern(regexp = "^[0-9]{3}$", message = "Il CVC deve contenere solo numeri.")
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
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Errore di validazione input: " + e.getMessage());
        }catch (RuntimeException e) {
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
