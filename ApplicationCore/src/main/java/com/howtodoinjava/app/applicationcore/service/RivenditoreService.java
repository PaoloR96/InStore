package com.howtodoinjava.app.applicationcore.service;
import com.howtodoinjava.app.applicationcore.dto.ProdottoDTO;
import com.howtodoinjava.app.applicationcore.entity.Cliente;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
                iban
        );
        rivenditoreRepository.save(rivenditore);

        return rivenditore;
    }


    public Rivenditore getRivenditore(String username) {
        return rivenditoreRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Rivenditore non trovato con username: " + username));
    }


    public String salvaImmagine(MultipartFile immagine) throws IOException {
        if (immagine.isEmpty()) {
            throw new IllegalArgumentException("Il file immagine non può essere vuoto.");
        }

        // Verifica il tipo di file
        String contentType = immagine.getContentType();
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            throw new IllegalArgumentException("Sono consentiti solo file JPEG o PNG.");
        }

        // Definisci la directory di destinazione (configurabile)
        String uploadDir = System.getProperty("user.dir") + "/uploads/images/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs(); // Crea la directory se non esiste
            if (!created) {
                throw new IOException("Impossibile creare la directory: " + uploadDir);
            }
        }

        // Genera un nome unico per il file
        String fileName = System.currentTimeMillis() + "_" + immagine.getOriginalFilename();
        String filePath = uploadDir + fileName;

        // Salva il file
        File dest = new File(filePath);
        immagine.transferTo(dest);

        // Restituisci il path relativo per l'accesso dal frontend
        return "/uploads/images/" + fileName;
    }
}

