package com.howtodoinjava.app.applicationcore.service;

import com.howtodoinjava.app.applicationcore.dto.ProdottoDTO;
import com.howtodoinjava.app.applicationcore.mapper.ProdottoMapper;
import com.howtodoinjava.app.applicationcore.utility.CarrelloResponse;
import com.howtodoinjava.app.applicationcore.entity.*;
import com.howtodoinjava.app.applicationcore.repository.*;
import com.howtodoinjava.app.applicationcore.utility.KeycloakRoles;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//TODO eliminare funzionalità di creazione cliente standard
@Service
public class ClienteService {

    @Autowired
    private ProdottoRepository prodottoRepository;
    @Autowired
    private CarrelloRepository carrelloRepository;
    @Autowired
    private ProdottoCarrelloRepository prodottoCarrelloRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private CartaCreditoRepository cartaCreditoRepository;
    @Autowired
    private ProdottoOrdineRepository prodottoOrdineRepository;
    @Autowired
    private OrdineRepository ordineRepository;

    private final KeycloakService keycloakService;
    private final ProdottoMapper prodottoMapper;

    public ClienteService(KeycloakService keycloakService, ProdottoMapper prodottoMapper) {
        this.keycloakService = keycloakService;
        this.prodottoMapper = prodottoMapper;
    }


    public List<ProdottoDTO> visualizzaProdotti(){
        List<Prodotto> prodotti = prodottoRepository.findAll();
        List<ProdottoDTO> prodottiDTO = prodotti.stream().map(this.prodottoMapper::prodottoToProdottoDTO).collect(Collectors.toList());
        return prodottiDTO;
    }

    @Transactional
    public Carrello aggiungiProdottoCarrello(String username, Long idProdotto, Integer quantita) {

        Carrello carrello = carrelloRepository.findByClienteUsername(username)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato per l'utente: " + username));

        Prodotto prodotto = prodottoRepository.findById(idProdotto)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato con id: " + idProdotto));

        // Controlla se il prodotto è già nel carrello
        ProdottoCarrelloId prodottoCarrelloId = new ProdottoCarrelloId(carrello.getCliente().getUsername(), prodotto.getIdProdotto());
        Optional<ProdottoCarrello> esistente = prodottoCarrelloRepository.findById(prodottoCarrelloId);

        if (esistente.isPresent()) {
            // Aggiorna la quantità se il prodotto è già presente nel carrello
            ProdottoCarrello prodottoCarrello = esistente.get();
            if(prodottoCarrello.getQuantita() + quantita <= prodotto.getQuantitaTotale()) {
                prodottoCarrello.setQuantita(prodottoCarrello.getQuantita() + quantita);
                prodottoCarrelloRepository.save(prodottoCarrello);
            }else {
                throw new IllegalArgumentException("Quantità richiesta (" + quantita + " + " + prodottoCarrello.getQuantita() + ") maggiore della quantità disponibile (" + prodotto.getQuantitaTotale() + ").");
            }

        } else {

            if(quantita<=prodotto.getQuantitaTotale()) {
                // Aggiungi un nuovo prodotto al carrello
                ProdottoCarrello nuovoProdottoCarrello = new ProdottoCarrello(
                        prodottoCarrelloId,
                        carrello,
                        prodotto,
                        quantita);

                prodottoCarrelloRepository.save(nuovoProdottoCarrello);
            }else {
                throw new IllegalArgumentException("Quantità richiesta (" + quantita + ") maggiore della quantità disponibile (" + prodotto.getQuantitaTotale() + ").");
            }
        }

        carrelloRepository.updatePrezzoComplessivoByUsername(username);
        //Per restituire in carrello il prezzoComplessivo giusto e aggiornato
        carrello.setPrezzoComplessivo(carrelloRepository.findPrezzoComplessivoByUsername(username));

        Hibernate.initialize(carrello.getListaProdottiCarrello());

        return carrello;
    }


    public CarrelloResponse visualizzaProdottiCarrello(String username) {
        // Recupera il carrello associato all'utente (username)
        Carrello carrello = carrelloRepository.findByClienteUsername(username)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato per l'utente: " + username));

        Cliente cliente = clienteRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato: " + username));

        // Recupera i prodotti presenti nel carrello
        List<ProdottoCarrello> prodottiCarrello = prodottoCarrelloRepository.findByCarrello(carrello);

        // Estrai i prodotti dalla lista di ProdottoCarrello
        List<Prodotto> prodotti = new ArrayList<>();
        for (ProdottoCarrello prodottoCarrello : prodottiCarrello) {
            prodotti.add(prodottoCarrello.getProdotto());
        }

        for (Prodotto prodotto : prodotti) {
            prodotto.setQuantitaTotale(prodottoCarrelloRepository.findQuantitaByProdottoAndCarrello(prodotto,carrello));
        }


        CarrelloResponse response = new CarrelloResponse();
        response.setProdotti(prodotti);
        response.setPrezzoTotale(getPrezzoComplessivoScontato(cliente,carrello));

        if (cliente instanceof ClientePremium) {
            response.setScontoApplicato(((ClientePremium) cliente).getSconto());
        }

        return response;
    }

    @Transactional
    public void rimuoviProdottoCarrello(String username, Long idProdotto) {

        Carrello carrello = carrelloRepository.findByClienteUsername(username)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato per l'utente: " + username));

        ProdottoCarrelloId prodottoCarrelloId = new ProdottoCarrelloId(carrello.getCliente().getUsername(), idProdotto);

        ProdottoCarrello prodottoCarrello = prodottoCarrelloRepository.findById(prodottoCarrelloId)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato nel carrello"));

        prodottoCarrelloRepository.delete(prodottoCarrello);

        carrelloRepository.updatePrezzoComplessivoByUsername(username);
    }

    @Transactional
    public void upgradePremium(String username){

        Cliente cliente = clienteRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Cliente non trovato con username: " + username));

        if (cliente instanceof ClientePremium) {
            throw new IllegalStateException("Il cliente è già PREMIUM");
        }

        clienteRepository.upgradeClientePremium(username, 10);
        keycloakService.addRole(username, KeycloakRoles.CLIENTE_PREMIUM);
    }


    @Transactional
    public void preparaOrdine(String username) {
        Cliente cliente = clienteRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Cliente non trovato"));

        Carrello carrello = carrelloRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato"));

        List<ProdottoCarrello> prodottiCarrello = getProdottiCarrelloAndCheckQuantity(carrello);

        Ordine ordine = new Ordine(
                new Date(),
                getPrezzoComplessivoScontato(cliente, carrello),
                cliente);

        ordine = ordineRepository.save(ordine);

        // Creazione prodotti ordine e aggiornamento quantità
        List<ProdottoOrdine> prodottiOrdine = createProdottiOrdineAndUpdateQuantity(prodottiCarrello, ordine);
        prodottoOrdineRepository.saveAll(prodottiOrdine);
        prodottoRepository.saveAll(prodottiCarrello.stream().map(ProdottoCarrello::getProdotto).toList());

    }

    @Transactional
    public Carrello preparaCarrello(String username) {

        return carrelloRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato"));
    }

    @Transactional
    public void svuotaCarrello(String username) {
        Carrello carrello = carrelloRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato"));

        List<ProdottoCarrello> prodottiCarrello = new ArrayList<>(carrello.getListaProdottiCarrello());

        // Svuotamento e azzeramento prezzo complessivo del carrello
        carrello.getListaProdottiCarrello().clear();
        carrelloRepository.save(carrello);
        prodottoCarrelloRepository.deleteAll(prodottiCarrello);
        carrelloRepository.updatePrezzoComplessivoByUsername(username);
    }

    private static List<ProdottoOrdine> createProdottiOrdineAndUpdateQuantity(List<ProdottoCarrello> prodottiCarrello, Ordine ordine) {
        List<ProdottoOrdine> prodottiOrdine = new ArrayList<>();
        for (ProdottoCarrello pc : prodottiCarrello) {
            Prodotto prodotto = pc.getProdotto();
            prodotto.setQuantitaTotale(prodotto.getQuantitaTotale() - pc.getQuantita());

            ProdottoOrdine prodottoOrdine = new ProdottoOrdine(
                    new ProdottoOrdineId(ordine.getIdOrdine(),
                            prodotto.getIdProdotto()),
                    ordine,
                    prodotto,
                    pc.getQuantita());

            prodottiOrdine.add(prodottoOrdine);
        }
        return prodottiOrdine;
    }

    private static List<ProdottoCarrello> getProdottiCarrelloAndCheckQuantity(Carrello carrello) {
        List<ProdottoCarrello> prodottiCarrello = carrello.getListaProdottiCarrello();
        if (prodottiCarrello.isEmpty()) {
            throw new RuntimeException("Il carrello è vuoto");
        }

        // Controllo disponibilità prodotti
        for (ProdottoCarrello pc : prodottiCarrello) {
            Prodotto prodotto = pc.getProdotto();
            if (pc.getQuantita() > prodotto.getQuantitaTotale()) {
                throw new RuntimeException("Quantità insufficiente per il prodotto: " + prodotto.getNomeProdotto());
            }
        }
        return prodottiCarrello;
    }


    public Float getPrezzoComplessivoScontato(Cliente cliente, Carrello carrello) {
        Float prezzoTotale;

        // TODO modify this check
        if (cliente instanceof ClientePremium) {
            prezzoTotale = carrello.getPrezzoComplessivo() -
                    carrello.getPrezzoComplessivo() * ((ClientePremium) cliente).getSconto() / 100;
        } else {
            prezzoTotale = carrello.getPrezzoComplessivo();
        }

        // Arrotondamento a due cifre decimali
        BigDecimal bd = new BigDecimal(prezzoTotale).setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    public Cliente getCliente(String username) {
        return clienteRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Cliente non trovato con username: " + username));
    }


    public Cliente creareClienteStandard(String username, String email, String numCell,
                                         String nome, String cognome, String numeroCarta, Date dataScadenza,
                                         String nomeIntestatario, String cognomeIntestatario, String cvc) {

        CartaCredito cartaCredito = new CartaCredito(
                numeroCarta,
                dataScadenza,
                nomeIntestatario,
                cognomeIntestatario,
                cvc);

        cartaCreditoRepository.save(cartaCredito); // Salviamo prima la carta di credito

        Cliente clienteStandard = new Cliente(
                email,
                username,
                numCell,
                nome,
                cognome,
                cartaCredito
//                StatoCliente.ABILITATO
        );

        Carrello carrello = new Carrello(clienteStandard, 0.0f);

        clienteStandard.setCarrello(carrello);

        //Salvare il Cliente (e il Carrello grazie a cascade.all)
        clienteRepository.save(clienteStandard);

        return clienteStandard;
    }

}

