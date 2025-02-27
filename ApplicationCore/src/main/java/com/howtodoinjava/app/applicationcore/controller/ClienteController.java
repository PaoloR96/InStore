package com.howtodoinjava.app.applicationcore.controller;

import com.howtodoinjava.app.applicationcore.utility.CarrelloResponse;
import com.howtodoinjava.app.applicationcore.entity.*;
import com.howtodoinjava.app.applicationcore.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

//TODO eliminare funzionalità di creazione cliente standard

@RestController
@RequestMapping("/api")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    //TODO changed this URI
    @GetMapping("/clienti/prodotti")
    public ResponseEntity<List<Prodotto>> visualizzaProdotti() {
        List<Prodotto> prodotti = clienteService.visualizzaProdotti();
        return ResponseEntity.ok(prodotti);
    }

    @PostMapping("/carrello/{username}/aggiungi")
    public ResponseEntity<?> aggiungiProdottoAlCarrello(
            @PathVariable String username,
            @RequestParam Long idProdotto,
            @RequestParam Integer quantita) {
        try {
            if (quantita <= 0) {
                return ResponseEntity.badRequest().body("La quantità deve essere maggiore di zero.");
            }

            Carrello carrello = clienteService.aggiungiProdottoCarrello(username, idProdotto, quantita);
            return ResponseEntity.ok(carrello);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno del server.");
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

    @GetMapping("/carrello/{username}/prodotti")
    public ResponseEntity<CarrelloResponse> visualizzaProdottiCarrello(@PathVariable String username) {
        // Recupera la lista dei prodotti nel carrello per il cliente specificato
        try {
            CarrelloResponse prodottiAndPrezzo = clienteService.visualizzaProdottiCarrello(username);
            return ResponseEntity.ok(prodottiAndPrezzo);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/carrello/{username}/rimuovi")
    public ResponseEntity<Void> rimuoviProdottoDalCarrello(
            @PathVariable String username,
            @RequestParam Long idProdotto) {
        try {
            clienteService.rimuoviProdottoCarrello(username, idProdotto);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/clienti/{username}/upgrade")
    public ResponseEntity<String> upgradePremium(@PathVariable String username) {
        try {
            clienteService.upgradePremium(username);
            return ResponseEntity.ok("Adesso sei un cliente PREMIUM");
        } catch (IllegalStateException e) {
            // Cliente già premium
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (RuntimeException e) {
            // Cliente non trovato
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/pagamento/{username}")
    public ResponseEntity<String> effettuaPagamento(@PathVariable String username) {
        try {
            clienteService.effettuaPagamento(username);
            return ResponseEntity.ok("Pagamento effettuato con successo");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/clienti/{username}")
    public ResponseEntity<Cliente> getClienteInfo(@PathVariable String username) {
        try {
            Cliente cliente = clienteService.getCliente(username);
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/clienti/aggiungi_standard")
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
