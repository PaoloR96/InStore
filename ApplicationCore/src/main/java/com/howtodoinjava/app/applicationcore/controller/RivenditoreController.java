package com.howtodoinjava.app.applicationcore.controller;

import com.howtodoinjava.app.applicationcore.dto.ProdottoDTO;
import com.howtodoinjava.app.applicationcore.service.RivenditoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rivenditori")
public class RivenditoreController {

    private final RivenditoreService rivenditoreService;

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(RivenditoreController.class);

    @Autowired
    public RivenditoreController(RivenditoreService rivenditoreService) {
        this.rivenditoreService = rivenditoreService;
    }

    @PostMapping("/{rivenditoreUsername}/insprodotti")
    public ResponseEntity<String> inserisciProdotto(
            @PathVariable String rivenditoreUsername,
            @RequestBody ProdottoDTO prodottoDTO) {
        logger.info("Ricevuta richiesta per inserire un prodotto per il rivenditore: {}", rivenditoreUsername);
        rivenditoreService.inserisciProdotto(rivenditoreUsername, prodottoDTO);
        logger.info("Prodotto inserito con successo per il rivenditore: {}", rivenditoreUsername);
        return ResponseEntity.ok("Prodotto inserito con successo.");
    }
    @GetMapping("/{rivenditoreUsername}/listaprodotti")
    public ResponseEntity<List<ProdottoDTO>> getProdottiByRivenditore(
            @PathVariable String rivenditoreUsername) {
        List<ProdottoDTO> prodotti = rivenditoreService.getProdottiByRivenditore(rivenditoreUsername);
        return ResponseEntity.ok(prodotti);
    }


}
