package com.howtodoinjava.app.applicationcore.controller;

import com.howtodoinjava.app.applicationcore.utility.CarrelloResponse;
import com.howtodoinjava.app.applicationcore.entity.*;
import com.howtodoinjava.app.applicationcore.service.ClienteService;
import com.howtodoinjava.app.applicationcore.utility.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

//TODO eliminare funzionalità di creazione cliente standard

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    //TODO changed this URI
    @GetMapping("/prodotti")
    public ResponseEntity<List<Prodotto>> visualizzaProdotti() {
        List<Prodotto> prodotti = clienteService.visualizzaProdotti();
        return ResponseEntity.ok(prodotti);
    }

    @PostMapping("/carrello/aggiungi")
    public ResponseEntity<?> aggiungiProdottoAlCarrello(
            @RequestParam Long idProdotto,
            @RequestParam Integer quantita,
            Authentication auth) {
        try {
            String username = JWTUtils.getUsername(auth);
            if (quantita <= 0) {
                return ResponseEntity.badRequest().body("La quantità deve essere maggiore di zero.");
            }

            Carrello carrello = clienteService.aggiungiProdottoCarrello(username, idProdotto, quantita);
            return ResponseEntity.ok(carrello);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


//    @GetMapping("/carrello/{username}/prodotti")
//    public ResponseEntity<List<ProdottoCarrello>> visualizzaProdottiCarrello(@PathVariable String username) {
//        try {
//            List<ProdottoCarrello> prodottiCarrello = clienteService.visualizzaProdottiCarrello(username);
//            return ResponseEntity.ok(prodottiCarrello);
//        } catch (RuntimeException e) {
//            System.out.println(e.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }

    @GetMapping("/carrello/prodotti")
    public ResponseEntity<CarrelloResponse> visualizzaProdottiCarrello(Authentication auth) {
        // Recupera la lista dei prodotti nel carrello per il cliente specificato
        try {
            String username = JWTUtils.getUsername(auth);
            CarrelloResponse prodottiAndPrezzo = clienteService.visualizzaProdottiCarrello(username);
            return ResponseEntity.ok(prodottiAndPrezzo);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/carrello/rimuovi")
    public ResponseEntity<Void> rimuoviProdottoDalCarrello(
            @RequestParam Long idProdotto,
            Authentication auth) {
        try {
            String username = JWTUtils.getUsername(auth);
            clienteService.rimuoviProdottoCarrello(username, idProdotto);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/upgrade")
    public ResponseEntity<String> upgradePremium(Authentication auth) {
        try {
            String username = JWTUtils.getUsername(auth);
            clienteService.upgradePremium(username);
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

    @GetMapping("/")
    public ResponseEntity<Cliente> getClienteInfo(Authentication auth) {
        try {
            String username = JWTUtils.getUsername(auth);
            Cliente cliente = clienteService.getCliente(username);
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/aggiungi_standard")
    public Cliente creareClienteStandard(@RequestParam String username,
                                         @RequestParam String email,
                                         @RequestParam String password,
                                         @RequestParam String numCell,
                                         @RequestParam String nome,
                                         @RequestParam String cognome,
                                         @RequestParam String numeroCarta,
                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataScadenza,
                                         @RequestParam String nomeIntestatario,
                                         @RequestParam String cognomeIntestatario,
                                         @RequestParam Integer cvc) {
        return clienteService.creareClienteStandard(username, email, password, numCell, nome, cognome, numeroCarta,
                dataScadenza, nomeIntestatario, cognomeIntestatario, cvc);
    }

//    @PostMapping("/clienti/aggiungi_premium")
//    public Cliente creareClientePremium(@RequestParam String username,
//                                        @RequestParam String email,
//                                        @RequestParam String password,
//                                        @RequestParam String numCell,
//                                        @RequestParam String nome,
//                                        @RequestParam String cognome,
//                                        @RequestParam Integer sconto,
//                                        @RequestParam String numeroCarta,
//                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataScadenza,
//                                        @RequestParam String nomeIntestatario,
//                                        @RequestParam String cognomeIntestatario,
//                                        @RequestParam Integer cvc) {
//        return clienteService.creareClientePremium(username, email, password, numCell, nome, cognome, sconto, numeroCarta, dataScadenza, nomeIntestatario, cognomeIntestatario, cvc);
//    }


}
