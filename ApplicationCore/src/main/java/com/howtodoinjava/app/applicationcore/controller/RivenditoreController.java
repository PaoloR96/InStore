package com.howtodoinjava.app.applicationcore.controller;



import com.howtodoinjava.app.applicationcore.dto.ProdottoDTO;
import com.howtodoinjava.app.applicationcore.entity.Prodotto;
import com.howtodoinjava.app.applicationcore.service.RivenditoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rivenditori")
public class RivenditoreController {

    private final RivenditoreService rivenditoreService;

    @Autowired
    public RivenditoreController(RivenditoreService rivenditoreService) {
        this.rivenditoreService = rivenditoreService;
    }

    @PostMapping("/{rivenditoreUsername}/prodotti")
    public ResponseEntity<String> inserisciProdotto(
            @PathVariable String rivenditoreUsername,
            @RequestBody ProdottoDTO prodottoDTO) {
        rivenditoreService.inserisciProdotto(rivenditoreUsername, prodottoDTO);
        return ResponseEntity.ok("Prodotto inserito con successo.");
    }



}

