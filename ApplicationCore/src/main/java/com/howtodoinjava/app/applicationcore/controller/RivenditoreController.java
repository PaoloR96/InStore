package com.howtodoinjava.app.applicationcore.controller;

import com.howtodoinjava.app.applicationcore.dto.ProdottoDTO;
import com.howtodoinjava.app.applicationcore.service.RivenditoreService;
import com.howtodoinjava.app.applicationcore.utility.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rivenditore")
public class RivenditoreController {

    private final RivenditoreService rivenditoreService;

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(RivenditoreController.class);

    @Autowired
    public RivenditoreController(RivenditoreService rivenditoreService) {
        this.rivenditoreService = rivenditoreService;
    }

    @PostMapping("/insprodotti")
    public ResponseEntity<String> inserisciProdotto(
            @RequestBody ProdottoDTO prodottoDTO,
            Authentication auth) {
        try {
            String username = JWTUtils.getUsername(auth);
            logger.info("Ricevuta richiesta per inserire un prodotto per il rivenditore: {}", username);
            rivenditoreService.inserisciProdotto(username, prodottoDTO);
            logger.info("Prodotto inserito con successo per il rivenditore: {}", username);
            return ResponseEntity.ok("Prodotto inserito con successo.");
        } catch (RuntimeException e) {
            logger.info(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/listaprodotti")
    public ResponseEntity<List<ProdottoDTO>> getProdottiByRivenditore(
            Authentication auth) {
        try{
            String username = JWTUtils.getUsername(auth);
            List<ProdottoDTO> prodotti = rivenditoreService.getProdottiByRivenditore(username);
            return ResponseEntity.ok(prodotti);
        } catch (RuntimeException e) {
            logger.info(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }


}
