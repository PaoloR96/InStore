package com.howtodoinjava.app.applicationcore.service;

import com.howtodoinjava.app.applicationcore.entity.Prodotto;
import com.howtodoinjava.app.applicationcore.entity.Carrello;
import com.howtodoinjava.app.applicationcore.entity.ProdottoCarrello;
import com.howtodoinjava.app.applicationcore.entity.ProdottoCarrelloId;
import com.howtodoinjava.app.applicationcore.repository.CarrelloRepository;
import com.howtodoinjava.app.applicationcore.repository.ClienteRepository;
import com.howtodoinjava.app.applicationcore.repository.ProdottoCarrelloRepository;
import com.howtodoinjava.app.applicationcore.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ProdottoRepository prodottoRepository;
    @Autowired
    private CarrelloRepository carrelloRepository;
    @Autowired
    private ProdottoCarrelloRepository prodottoCarrelloRepository;

    public List<Prodotto> visualizzaProdotti(){
        return prodottoRepository.findAll();
    }

    public Carrello aggiungiProdottoAlCarrello(String username, Long idProdotto, Integer quantita) {
        // Recupera il carrello del cliente
        Carrello carrello = carrelloRepository.findByClienteUsername(username)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato per l'utente: " + username));

        // Recupera il prodotto
        Prodotto prodotto = prodottoRepository.findById(idProdotto)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato con id: " + idProdotto));

        // Controlla se il prodotto è già nel carrello
        ProdottoCarrelloId prodottoCarrelloId = new ProdottoCarrelloId(carrello.getCliente().getUsername(), prodotto.getIdProdotto());
        Optional<ProdottoCarrello> esistente = prodottoCarrelloRepository.findById(prodottoCarrelloId);

        if (esistente.isPresent()) {
            // Aggiorna la quantità se il prodotto è già presente nel carrello
            ProdottoCarrello prodottoCarrello = esistente.get();
            prodottoCarrello.setQuantita(prodottoCarrello.getQuantita() + quantita);
            prodottoCarrelloRepository.save(prodottoCarrello);
        } else {
            // Aggiungi un nuovo prodotto al carrello
            ProdottoCarrello nuovoProdottoCarrello = new ProdottoCarrello();
            nuovoProdottoCarrello.setId(prodottoCarrelloId);
            nuovoProdottoCarrello.setCarrello(carrello);
            nuovoProdottoCarrello.setProdotto(prodotto);
            nuovoProdottoCarrello.setQuantita(quantita);
            prodottoCarrelloRepository.save(nuovoProdottoCarrello);
        }

        return carrello;
    }

    public List<ProdottoCarrello> visualizzaProdottiCarrello(String username) {
        // Verifica se il carrello esiste per l'utente
        Carrello carrello = carrelloRepository.findByClienteUsername(username)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato per l'utente: " + username));

        // Recupera i prodotti associati al carrello
        return prodottoCarrelloRepository.findByCarrelloClienteUsername(username);
    }

}
