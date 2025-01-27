package com.howtodoinjava.app.applicationcore.service;

import com.howtodoinjava.app.applicationcore.entity.Prodotto;
import com.howtodoinjava.app.applicationcore.entity.Carrello;
import com.howtodoinjava.app.applicationcore.repository.CarrelloRepository;
import com.howtodoinjava.app.applicationcore.repository.ClienteRepository;
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

    public List<Prodotto> visualizzaProdotti(){
        return prodottoRepository.findAll();
    }

//    public Carrello aggiungiProdottoAlCarrello(Long idCarrello, Long idProdotto) {
//        Optional<Carrello> carrelloOptional = carrelloRepository.findById(Math.toIntExact(idCarrello));
//        if (carrelloOptional.isEmpty()) {
//            throw new RuntimeException("Carrello non trovato con ID: " + idCarrello);
//        }
//        Carrello carrello = carrelloOptional.get();
//
//        // Trova il prodotto
//        Optional<Prodotto> prodottoOptional = prodottoRepository.findById(idProdotto);
//        if (prodottoOptional.isEmpty()) {
//            throw new RuntimeException("Prodotto non trovato con ID: " + idProdotto);
//        }
//        Prodotto prodotto = prodottoOptional.get();
//
////        // Aggiungi il prodotto alla lista del carrello
////        carrello.getListaProdottiCarrello().add(prodotto);
////
////        // Aggiorna il prezzo complessivo
////        carrello.setPrezzoComplessivo(carrello.getPrezzoComplessivo() + prodotto.getPrezzo());
//
//        prodottoRepository.updateIdCarrello(idProdotto, idCarrello);
//        // Salva il carrello aggiornato
//        return carrelloRepository.save(carrello);
//    }
//
//
//    public List<Prodotto> visualizzaCarrello(Long idCarrello) {
//        return prodottoRepository.findByCarrelloId(idCarrello);
//    }

}
