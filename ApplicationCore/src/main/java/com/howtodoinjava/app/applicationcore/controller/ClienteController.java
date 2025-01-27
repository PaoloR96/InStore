package com.howtodoinjava.app.applicationcore.controller;

import com.howtodoinjava.app.applicationcore.entity.Carrello;
import com.howtodoinjava.app.applicationcore.entity.Prodotto;
import com.howtodoinjava.app.applicationcore.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/prodotti")
    public List<Prodotto> getTuttiProdotti() {
        return clienteService.visualizzaProdotti();
    }

//    @PostMapping("/carrello/{idCarrello}/prodotti/{idProdotto}")
//    public Carrello aggiungiProdottoAlCarrello(@PathVariable Long idCarrello, @PathVariable Long idProdotto) {
//        return clienteService.aggiungiProdottoAlCarrello(idCarrello, idProdotto);
//    }

}
