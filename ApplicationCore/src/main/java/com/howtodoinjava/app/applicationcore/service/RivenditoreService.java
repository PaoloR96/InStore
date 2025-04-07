package com.howtodoinjava.app.applicationcore.service;
import com.howtodoinjava.app.applicationcore.dto.ProdottoDTO;
import com.howtodoinjava.app.applicationcore.entity.Prodotto;
import com.howtodoinjava.app.applicationcore.entity.Rivenditore;
import com.howtodoinjava.app.applicationcore.entity.StatoRivenditore;
import com.howtodoinjava.app.applicationcore.mapper.ProdottoMapper;
import com.howtodoinjava.app.applicationcore.repository.ProdottoRepository;
import com.howtodoinjava.app.applicationcore.repository.RivenditoreRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RivenditoreService {

    private final RivenditoreRepository rivenditoreRepository;
    private final ProdottoRepository prodottoRepository;
    private final ProdottoMapper prodottoMapper;


    // Logger
    private static final Logger logger = LoggerFactory.getLogger(RivenditoreService.class);

    @Autowired

    public RivenditoreService(RivenditoreRepository rivenditoreRepository,
                              ProdottoRepository prodottoRepository, ProdottoMapper prodottoMapper) {
        this.rivenditoreRepository = rivenditoreRepository;
        this.prodottoRepository = prodottoRepository;
        this.prodottoMapper = prodottoMapper;

    }

    public void inserisciProdotto(String rivenditoreUsername, ProdottoDTO prodottoDTO) {
        logger.info("Inizio inserimento prodotto per il rivenditore con username: {}", rivenditoreUsername);

        // Recupera il rivenditore dal database
        Rivenditore rivenditore = rivenditoreRepository.findByUsername(rivenditoreUsername)
                .orElseThrow(() -> {
                    logger.error("Rivenditore non trovato con username: {}", rivenditoreUsername);
                    return new IllegalArgumentException("Rivenditore non trovato con username: " + rivenditoreUsername);
                });

        // Utilizza il mapper per convertire il DTO in entity
        Prodotto prodotto = prodottoMapper.prodottoDTOToProdotto(prodottoDTO);
        prodotto.setRivenditore(rivenditore);

        // Salva il prodotto nel database
        prodottoRepository.save(prodotto);
        logger.info("Prodotto inserito con successo per il rivenditore {}", rivenditoreUsername);
    }

    @Transactional
    public List<ProdottoDTO> getProdottiByRivenditore(String rivenditoreUsername) {
        logger.info("Inizio recupero prodotti per il rivenditore con username: {}", rivenditoreUsername);

        // Recupera il rivenditore dal database
        Rivenditore rivenditore = rivenditoreRepository.findByUsername(rivenditoreUsername)
                .orElseThrow(() -> {
                    logger.error("Rivenditore non trovato con username: {}", rivenditoreUsername);
                    return new IllegalArgumentException("Rivenditore non trovato con username: " + rivenditoreUsername);
                });

        logger.info("Rivenditore trovato con username: {}, numero prodotti: {}",
                rivenditoreUsername, rivenditore.getListaProdottiRivenditore().size());

        // Converte la lista di entità in DTO usando il mapper
        List<ProdottoDTO> prodottoDTOList = rivenditore.getListaProdottiRivenditore()
                .stream()
                .map(prodottoMapper::prodottoToProdottoDTO)
                .collect(Collectors.toList());

        logger.info("Recuperati {} prodotti per il rivenditore {}", prodottoDTOList.size(), rivenditoreUsername);
        return prodottoDTOList;
    }

    public Rivenditore createRivenditore(
          String email,
          String username,
          String phoneNumber,
          String nomeSocieta,
          String partitaIva,
          String iban
    ){
        Rivenditore rivenditore = new Rivenditore(
                email,
                username,
                phoneNumber,
                nomeSocieta,
                partitaIva,
                iban,
                new ArrayList<>(),
                StatoRivenditore.ABILITATO
        );
        rivenditoreRepository.save(rivenditore);

        return rivenditore;
    }


}
