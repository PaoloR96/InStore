package com.howtodoinjava.app.applicationcore.controller;

import com.howtodoinjava.app.applicationcore.entity.Prodotto;
import com.howtodoinjava.app.applicationcore.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/prodotti")
    public String visualizzaProdotti(Model model) {
        // Recupera la lista di prodotti dal servizio
        List<Prodotto> prodotti = clienteService.visualizzaProdotti();
        // Aggiungi i prodotti al modello per renderli accessibili nel template
        model.addAttribute("prodotti", prodotti);
        return "prodotti"; // Nome del file HTML (prodotti.html)
    }


}
