package com.howtodoinjava.app.applicationcore.controller;

import com.howtodoinjava.app.applicationcore.dto.ClienteDTO;
import com.howtodoinjava.app.applicationcore.dto.ProdottoDTO;
import com.howtodoinjava.app.applicationcore.mapper.ProdottoMapper;
import com.howtodoinjava.app.applicationcore.utility.CarrelloResponse;
import com.howtodoinjava.app.applicationcore.entity.*;
import com.howtodoinjava.app.applicationcore.service.ClienteService;
import com.howtodoinjava.app.applicationcore.utility.JWTUtils;
import jakarta.persistence.DiscriminatorValue;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO eliminare funzionalità di creazione cliente standard

@RestController
@RequestMapping("/cliente/api")
@Validated
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    //TODO changed this URI
    @GetMapping("/prodotti")
    public ResponseEntity<List<ProdottoDTO>> visualizzaProdotti(ProdottoMapper prodottoMapper) {
        List<ProdottoDTO> prodotti = clienteService.visualizzaProdotti();
        //TODO aggiungere l'escape ??
        return ResponseEntity.ok(prodotti);
    }

    @PostMapping("/carrello/aggiungi")
    public ResponseEntity<?> aggiungiProdottoAlCarrello(
            @NotNull(message = "L'ID del prodotto non può essere nullo.")
            @Min(value = 1, message = "L'ID del prodotto deve essere un numero positivo.")
            @RequestParam Long idProdotto,
            @NotNull(message = "La quantità non può essere nulla.")
            @Min(value = 1, message = "La quantità deve essere maggiore di zero.")
            @RequestParam Integer quantita,
            Authentication auth) {
//        try {
//
//            if (quantita <= 0) {
//                return ResponseEntity.badRequest().body("La quantità deve essere maggiore di zero.");
//            }
        try{
            String username = JWTUtils.getUsername(auth);
            Carrello carrello = clienteService.aggiungiProdottoCarrello(username, idProdotto, quantita);
            carrello.escape();
            return ResponseEntity.ok(carrello);

        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Errore di validazione input: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }


    @GetMapping("/carrello/prodotti")
    public ResponseEntity<CarrelloResponse> visualizzaProdottiCarrello(Authentication auth) {
        // Recupera la lista dei prodotti nel carrello per il cliente specificato
        try {
            String username = JWTUtils.getUsername(auth);
            CarrelloResponse prodottiAndPrezzo = clienteService.visualizzaProdottiCarrello(username);
            prodottiAndPrezzo.escape();
            return ResponseEntity.ok(prodottiAndPrezzo);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/carrello/rimuovi")
    public ResponseEntity<?> rimuoviProdottoDalCarrello(
            @NotNull(message = "L'ID del prodotto non può essere nullo.")
            @Min(value = 1, message = "L'ID del prodotto deve essere un numero positivo.")
            @RequestParam Long idProdotto,
            Authentication auth) {
        try {
            String username = JWTUtils.getUsername(auth);
            clienteService.rimuoviProdottoCarrello(username, idProdotto);
            return ResponseEntity.noContent().build();
        } catch (ConstraintViolationException e) { // Cattura le eccezioni di validazione qui
            return ResponseEntity.badRequest().body("Errore di validazione input: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/upgrade")
    public ResponseEntity<String> upgradePremium(Authentication auth, HttpSession httpSession) {
        try {
            String username = JWTUtils.getUsername(auth);
            clienteService.upgradePremium(username);
            httpSession.invalidate();
            return ResponseEntity.ok("Adesso sei un cliente PREMIUM");
        } catch (IllegalStateException e) {
            // Cliente già premium
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            // Cliente non trovato
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/pagamento")
    public ResponseEntity<String> effettuaPagamento(Authentication auth) {
        try {
            String username = JWTUtils.getUsername(auth);
            clienteService.effettuaPagamento(username);
            return ResponseEntity.ok("Pagamento effettuato con successo");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/info")
    public ResponseEntity<ClienteDTO> getClienteInfo(Authentication auth) {
        try {
            String username = JWTUtils.getUsername(auth);
            Cliente cliente = clienteService.getCliente(username);
            ClienteDTO clienteDTO = new ClienteDTO(cliente);
            clienteDTO.escape();
            return ResponseEntity.ok(clienteDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/tipo")
    public ResponseEntity<String> getTipoCliente(Authentication auth) {
        try {
            String username = JWTUtils.getUsername(auth);
            Cliente cliente = clienteService.getCliente(username);
            String tipo = cliente.getClass().getAnnotation(DiscriminatorValue.class).value();
            return ResponseEntity.ok(tipo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
