package com.howtodoinjava.app.applicationcore.service;

import com.howtodoinjava.app.applicationcore.dto.ProdottoDTO;
import com.howtodoinjava.app.applicationcore.entity.Prodotto;
import com.howtodoinjava.app.applicationcore.entity.Rivenditore;
import com.howtodoinjava.app.applicationcore.repository.ProdottoRepository;
import com.howtodoinjava.app.applicationcore.repository.RivenditoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RivenditoreService {

    private final RivenditoreRepository rivenditoreRepository;
    private final ProdottoRepository prodottoRepository;

    @Autowired
    public RivenditoreService(RivenditoreRepository rivenditoreRepository, ProdottoRepository prodottoRepository) {
        this.rivenditoreRepository = rivenditoreRepository;
        this.prodottoRepository = prodottoRepository;
    }

    public void inserisciProdotto(String rivenditoreUsername, ProdottoDTO prodottoDTO) {
        // Recupera il rivenditore dal database
        Rivenditore rivenditore = rivenditoreRepository.findByUsername(rivenditoreUsername)
                .orElseThrow(() -> new IllegalArgumentException("Rivenditore non trovato con username: " + rivenditoreUsername));

        // Converte il DTO in entità Prodotto
        Prodotto prodotto = new Prodotto();
        prodotto.setNomeProdotto(prodottoDTO.getNomeProdotto());
        prodotto.setDescrizione(prodottoDTO.getDescrizione());
        prodotto.setPrezzo(prodottoDTO.getPrezzo());
        prodotto.setTaglia(prodottoDTO.getTaglia());
        prodotto.setImmagine(prodottoDTO.getImmagine());
        prodotto.setPathImmagine(prodottoDTO.getPathImmagine());
        prodotto.setQuantitaTotale(prodottoDTO.getQuantitaTotale());
        prodotto.setRivenditore(rivenditore);

        // Salva il prodotto nel database
        prodottoRepository.save(prodotto);
    }


}
