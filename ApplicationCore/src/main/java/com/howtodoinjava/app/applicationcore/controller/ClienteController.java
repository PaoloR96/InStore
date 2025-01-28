package com.howtodoinjava.app.applicationcore.controller;

import com.howtodoinjava.app.applicationcore.entity.Carrello;
import com.howtodoinjava.app.applicationcore.entity.Prodotto;
import com.howtodoinjava.app.applicationcore.entity.ProdottoCarrello;
import com.howtodoinjava.app.applicationcore.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;


    @GetMapping("/prodotti")
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

            Carrello carrello = clienteService.aggiungiProdottoAlCarrello(username, idProdotto, quantita);
            return ResponseEntity.ok(carrello);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno del server.");
        }
    }


    @GetMapping("/carrello/{username}/prodotti")
    public ResponseEntity<List<ProdottoCarrello>> visualizzaProdottiCarrello(@PathVariable String username) {
        try {
            List<ProdottoCarrello> prodottiCarrello = clienteService.visualizzaProdottiCarrello(username);
            return ResponseEntity.ok(prodottiCarrello);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
