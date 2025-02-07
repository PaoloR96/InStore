package com.howtodoinjava.app.applicationcore.service;

import com.howtodoinjava.app.applicationcore.entity.*;
import com.howtodoinjava.app.applicationcore.repository.*;
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


    public List<Prodotto> visualizzaProdotti(){
        return prodottoRepository.findAll();
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

    public List<ProdottoCarrello> visualizzaProdottiCarrello(String username) {

        carrelloRepository.findByClienteUsername(username).orElseThrow(() -> new RuntimeException("Carrello non trovato per l'utente: " + username));

        // Recupera i prodotti associati al carrello
        return prodottoCarrelloRepository.findByCarrelloClienteUsername(username);
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
    public Cliente upgradePremium(String username) {

        Cliente cliente = clienteRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Cliente non trovato con username: " + username));

        if (cliente instanceof ClientePremium) {
            throw new IllegalStateException("Il cliente è già PREMIUM");
        }

        clienteRepository.upgradeClientePremium(username, 10);

        return cliente;
    }


    @Transactional
    public void effettuaPagamento(String username) {

        Cliente cliente = clienteRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Cliente non trovato"));

        Carrello carrello = carrelloRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato"));

        List<ProdottoCarrello> prodottiCarrello = getProdottiCarrelloAndCheckQuantity(carrello);

        Ordine ordine = new Ordine(
                new Date(),
                applicaSconto(cliente,carrello),
                cliente);

        ordine = ordineRepository.save(ordine);

        // Creazione prodotti ordine e aggiornamento quantità
        List<ProdottoOrdine> prodottiOrdine = createProdottiOrdineAndUpdateQuantity(prodottiCarrello, ordine);

        prodottoOrdineRepository.saveAll(prodottiOrdine);
        prodottoRepository.saveAll(prodottiCarrello.stream().map(ProdottoCarrello::getProdotto).toList());

        // Svuotamento e azzeramento prezzo complessivo del carrello
        carrello.getListaProdottiCarrello().clear();
        carrelloRepository.save(carrello);
        prodottoCarrelloRepository.deleteAll(prodottiCarrello);
        carrelloRepository.updatePrezzoComplessivoByUsername(username);

        if(!eseguiPagamento(ordine)){
            throw new RuntimeException("Errore di Pagamento");
        }
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

    private Boolean eseguiPagamento(Ordine ordine) {
            return true;
    }

    private Float applicaSconto(Cliente cliente, Carrello carrello) {
        Float prezzoTotale;

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

//TODO eliminare queste funzioni commentate

//    @Transactional
//    public ClientePremium upgradePremium(String username) {
//        Cliente cliente = clienteRepository.findById(username)
//                .orElseThrow(() -> new RuntimeException("Cliente non trovato con username: " + username));
//        System.out.println("Cliente trovato: " + cliente.getUsername());
//
//        if (cliente instanceof ClientePremium) {
//            throw new IllegalStateException("Il cliente è già PREMIUM");
//        }
//
//        clienteRepository.delete(cliente);
//
//        ClientePremium clientePremium = creareClientePremium(
//                cliente.getUsername(),      // username del Cliente
//                cliente.getEmail(),         // email del Cliente
//                cliente.getPassword(),      // password del Cliente
//                cliente.getNumCell(),       // numero di cellulare
//                cliente.getNome(),          // nome del Cliente
//                cliente.getCognome(),       // cognome del Cliente
//                10,                         // sconto predefinito per il ClientePremium
//                cliente.getCartaCredito().getNumeroCarta(),  // numero carta di credito
//                cliente.getCartaCredito().getDataScadenza(), // data di scadenza della carta
//                cliente.getCartaCredito().getNomeIntestatario(),  // nome intestatario carta
//                cliente.getCartaCredito().getCognomeIntestatario(), // cognome intestatario carta
//                cliente.getCartaCredito().getCvc(), // cvc della carta di credito
//                cliente.getListaClienteOrdini()
//        );
//
//        return clientePremium;
//    }


//    public ClientePremium creareClientePremium(String username, String email, String password, String numCell,
//                                        String nome, String cognome, Integer sconto, String numeroCarta,
//                                        Date dataScadenza, String nomeIntestatario, String cognomeIntestatario,
//                                        Integer cvc, List<Ordine> listaClienteOrdini) {
//        //Creare la Carta di Credito
//        CartaCredito cartaCredito = new CartaCredito();
//        cartaCredito.setNumeroCarta(numeroCarta);
//        cartaCredito.setDataScadenza(dataScadenza);
//        cartaCredito.setNomeIntestatario(nomeIntestatario);
//        cartaCredito.setCognomeIntestatario(cognomeIntestatario);
//        cartaCredito.setCvc(cvc);
//        cartaCreditoRepository.save(cartaCredito); // Salviamo la carta di credito
//
//
//        //Creare il Cliente Premium
//        ClientePremium clientePremium = new ClientePremium();
//        clientePremium.setUsername(username);
//        clientePremium.setEmail(email);
//        clientePremium.setPassword(password);
//        clientePremium.setNumCell(numCell);
//        clientePremium.setNome(nome);
//        clientePremium.setCognome(cognome);
//        clientePremium.setStatoCliente(StatoCliente.ABILITATO); // Stato iniziale
//        clientePremium.setSconto(sconto); // Sconto per cliente premium
//        clientePremium.setCartaCredito(cartaCredito); // Associare la Carta di Credito
//        clientePremium.setListaClienteOrdini(listaClienteOrdini);
//
//        // Creare il Carrello
//        Carrello carrello = new Carrello();
//        carrello.setPrezzoComplessivo(0.0f); // Prezzo iniziale
//        carrello.setCliente(clientePremium); // Associare il cliente al carrello
//        clientePremium.setCarrello(carrello); // Associare il carrello al cliente
//
//        // Salvare il Cliente Premium (e il Carrello grazie alla cascata)
//        clienteRepository.save(clientePremium); // Salva sia Cliente che Carrello grazie alla cascata
//
//        return clientePremium;
//    }


    public Cliente creareClienteStandard(String username, String email, String password, String numCell,
                                         String nome, String cognome, String numeroCarta, Date dataScadenza,
                                         String nomeIntestatario, String cognomeIntestatario, Integer cvc) {

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
                password,
                numCell,
                nome,
                cognome,
                cartaCredito,
                StatoCliente.ABILITATO);

        Carrello carrello = new Carrello(clienteStandard, 0.0f);

        clienteStandard.setCarrello(carrello);

        //Salvare il Cliente (e il Carrello grazie a cascade.all)
        clienteRepository.save(clienteStandard);

        return clienteStandard;
    }
}

