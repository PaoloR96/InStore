package com.howtodoinjava.app.applicationcore.controller;

import com.howtodoinjava.app.applicationcore.dto.ClienteDTO;
import com.howtodoinjava.app.applicationcore.entity.PaymentRequest;
import com.howtodoinjava.app.applicationcore.entity.PaymentResponse;
import com.howtodoinjava.app.applicationcore.dto.ProdottoDTO;
import com.howtodoinjava.app.applicationcore.mapper.ProdottoMapper;
import com.howtodoinjava.app.applicationcore.service.PayPalService;
import com.howtodoinjava.app.applicationcore.utility.CarrelloResponse;
import com.howtodoinjava.app.applicationcore.entity.*;
import com.howtodoinjava.app.applicationcore.service.ClienteService;
import com.howtodoinjava.app.applicationcore.utility.JWTUtils;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.persistence.DiscriminatorValue;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.util.List;

//TODO eliminare funzionalità di creazione cliente standard

@RestController
@RequestMapping("/cliente/api")
@Validated
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    private PayPalService payPalService;

    //TODO changed this URI
    @GetMapping("/prodotti")
    public ResponseEntity<List<ProdottoDTO>> visualizzaProdotti(ProdottoMapper prodottoMapper) {
        List<ProdottoDTO> prodotti = clienteService.visualizzaProdotti();
        //TODO aggiungere l'escape ??
        return ResponseEntity.ok(prodotti);
    }

    @PostMapping("/carrello/aggiungi")
    public ResponseEntity<?> aggiungiProdottoAlCarrello(
            @NotNull(message = "L'ID del prodotto non può essere nullo.")
            @Min(value = 1, message = "L'ID del prodotto deve essere un numero positivo.")
            @RequestParam Long idProdotto,
            @NotNull(message = "La quantità non può essere nulla.")
            @Min(value = 1, message = "La quantità deve essere maggiore di zero.")
            @RequestParam Integer quantita,
            Authentication auth) {
//        try {
//
//            if (quantita <= 0) {
//                return ResponseEntity.badRequest().body("La quantità deve essere maggiore di zero.");
//            }
        try{
            String username = JWTUtils.getUsername(auth);
            Carrello carrello = clienteService.aggiungiProdottoCarrello(username, idProdotto, quantita);
            carrello.escape();
            return ResponseEntity.ok(carrello);

        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body("Errore di validazione input: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }


    @GetMapping("/carrello/prodotti")
    public ResponseEntity<CarrelloResponse> visualizzaProdottiCarrello(Authentication auth) {
        // Recupera la lista dei prodotti nel carrello per il cliente specificato
        try {
            String username = JWTUtils.getUsername(auth);
            CarrelloResponse prodottiAndPrezzo = clienteService.visualizzaProdottiCarrello(username);
            prodottiAndPrezzo.escape();
            return ResponseEntity.ok(prodottiAndPrezzo);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/carrello/rimuovi")
    public ResponseEntity<?> rimuoviProdottoDalCarrello(
            @NotNull(message = "L'ID del prodotto non può essere nullo.")
            @Min(value = 1, message = "L'ID del prodotto deve essere un numero positivo.")
            @RequestParam Long idProdotto,
            Authentication auth) {
        try {
            String username = JWTUtils.getUsername(auth);
            clienteService.rimuoviProdottoCarrello(username, idProdotto);
            return ResponseEntity.noContent().build();
        } catch (ConstraintViolationException e) { // Cattura le eccezioni di validazione qui
            return ResponseEntity.badRequest().body("Errore di validazione input: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/upgrade")
    public ResponseEntity<String> upgradePremium(Authentication auth, HttpSession httpSession) {
        try {
            String username = JWTUtils.getUsername(auth);
            clienteService.upgradePremium(username);
            httpSession.invalidate();
            return ResponseEntity.ok("Adesso sei un cliente PREMIUM");
        } catch (IllegalStateException e) {
            // Cliente già premium
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            // Cliente non trovato
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

@PostMapping("/pagamento/create")
public ResponseEntity<?> createPayment(Authentication auth, @RequestBody PaymentRequest paymentRequest) {
    logger.info("Ricevuta richiesta di creazione pagamento: {}", paymentRequest);

    try {
        String username = JWTUtils.getUsername(auth);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        BigDecimal totalAmount;
        String orderId;
        String customerEmail;
        String customerName;

        // Crea l'ordine basato sul carrello reale
        try {
//            Ordine ordine = clienteService.preparaOrdine(username);
//            totalAmount = BigDecimal.valueOf(ordine.getPrezzoComplessivo());
//            orderId = String.valueOf(ordine.getIdOrdine());
//            customerEmail = ordine.getCliente().getEmail();
//            customerName = ordine.getCliente().getUsername();
            Carrello carrello = clienteService.preparaCarrello(username);
            totalAmount = BigDecimal.valueOf(carrello.getPrezzoComplessivo());
            orderId = String.valueOf(carrello.getId());
            customerEmail = carrello.getCliente().getEmail();
            customerName = carrello.getCliente().getUsername();

            logger.info("Ordine creato per il pagamento - ID: {}, Importo: {}", orderId, totalAmount);

        } catch (RuntimeException e) {
            if (e.getMessage().contains("carrello è vuoto") || e.getMessage().contains("Carrello non trovato")) {
                logger.warn("Carrello vuoto o non trovato per l'utente: {}", username);
                return ResponseEntity.badRequest().body("Il carrello è vuoto. Aggiungi alcuni prodotti prima di procedere al pagamento.");
            }
            logger.error("Errore durante la preparazione dell'ordine: {}", e.getMessage());
            throw e;
        }

        // Validazione dell'importo
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("Importo non valido: {}", totalAmount);
            return ResponseEntity.badRequest().body("L'importo deve essere maggiore di zero");
        }

        // Impostazione dei valori nella richiesta
        paymentRequest.setTotal(totalAmount);
        paymentRequest.setCurrency(paymentRequest.getCurrency() != null ?
                paymentRequest.getCurrency() : "EUR");
        paymentRequest.setMethod(paymentRequest.getMethod() != null ?
                paymentRequest.getMethod() : "paypal");
        paymentRequest.setIntent(paymentRequest.getIntent() != null ?
                paymentRequest.getIntent() : "sale");
        paymentRequest.setCancelUrl(paymentRequest.getCancelUrl() != null ?
                paymentRequest.getCancelUrl() : baseUrl + "/cliente/api/pagamento/cancel");
        paymentRequest.setSuccessUrl(paymentRequest.getSuccessUrl() != null ?
                paymentRequest.getSuccessUrl() : baseUrl + "/cliente/api/pagamento/success");

        // Imposta i dati del cliente e dell'ordine
        paymentRequest.setOrderId(orderId);
        paymentRequest.setCustomerEmail(customerEmail);
        paymentRequest.setCustomerName(customerName);

        logger.debug("PaymentRequest completa: {}", paymentRequest);

        PaymentResponse response = payPalService.createPayment(paymentRequest);
        logger.info("Pagamento creato con successo - PaymentId: {}, OrderId: {}",
                response.getPaymentId(), orderId);

        return ResponseEntity.ok(response);

    } catch (IllegalArgumentException e) {
        logger.error("Errore di validazione: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (PayPalRESTException e) {
        logger.error("Errore PayPal durante la creazione del pagamento", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Errore PayPal: " + e.getMessage());
    } catch (Exception e) {
        logger.error("Errore generico durante la creazione del pagamento", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Errore interno del server: " + e.getMessage());
    }
}

    @GetMapping("/pagamento/redirect/{paymentId}")
    public RedirectView redirectToPayPal(@PathVariable String paymentId) {
        logger.info("Reindirizzamento per pagamento ID: {}", paymentId);

        try {
            String approvalUrl = payPalService.getApprovalUrl(paymentId);
            logger.debug("URL di approvazione PayPal: {}", approvalUrl);
            return new RedirectView(approvalUrl);
        } catch (Exception e) {
            logger.error("Errore durante il reindirizzamento a PayPal per il pagamento {}", paymentId, e);
            return new RedirectView("/cliente/api/pagamento/error");
        }
    }

    @GetMapping("/pagamento/success")
    public ModelAndView paymentSuccess(Authentication auth,
                                       @RequestParam(required = false) String paymentId,
                                       @RequestParam(required = false) String PayerID) {

        logger.info("Richiesta successo pagamento - paymentId: {}, PayerID: {}", paymentId, PayerID);

        if (paymentId != null && PayerID != null) {
            try {
                String username = JWTUtils.getUsername(auth);

                logger.info("Esecuzione pagamento da callback successo");
                ExecutePaymentResponse response = payPalService.executePayment(paymentId, PayerID);
                logger.info("Pagamento eseguito con successo da callback, ID transazione: {}", response.getTransactionId());

                // Finalizza l'ordine solo se il pagamento è stato approvato
                if ("approved".equalsIgnoreCase(response.getState())) {
                    try {
                        //pagamento avvenuto,preparo l'ordine
                        clienteService.preparaOrdine(username);
                        clienteService.svuotaCarrello(username);
                        logger.info("Carrello svuotato per l'utente: {} dopo pagamento completato", username);
                    } catch (Exception e) {
                        logger.error("Errore durante lo svuotamento del carrello", e);
                        // Non bloccare il flusso
                    }
                }

                ModelAndView modelAndView = new ModelAndView("cliente/payment-success");
                modelAndView.addObject("transactionId", response.getTransactionId());
                modelAndView.addObject("state", response.getState());
                modelAndView.addObject("paymentId", paymentId);
                modelAndView.addObject("message", "Pagamento completato con successo! Il tuo ordine è stato elaborato.");
                return modelAndView;

            } catch (Exception e) {
                logger.error("Errore durante l'esecuzione del pagamento da callback successo", e);

                ModelAndView modelAndView = new ModelAndView("cliente/payment-error");
                modelAndView.addObject("errorMessage", "Errore durante il completamento del pagamento: " + e.getMessage());
                return modelAndView;
            }
        }

        logger.info("Pagamento completato senza parametri (redirect semplice)");
        ModelAndView modelAndView = new ModelAndView("cliente/payment-success");
        modelAndView.addObject("transactionId", null);
        modelAndView.addObject("state", "completato");
        modelAndView.addObject("paymentId", null);
        modelAndView.addObject("message", "Pagamento in elaborazione. Controlla la tua email per la conferma.");
        return modelAndView;
    }

    @GetMapping("/pagamento/cancel")
    public ModelAndView paymentCancel() {
        logger.info("Pagamento cancellato dall'utente");
        ModelAndView modelAndView = new ModelAndView("cliente/payment-cancel");
        modelAndView.addObject("message", "Il pagamento è stato cancellato. Puoi riprovare quando vuoi.");
        return modelAndView;
    }

    @GetMapping("/pagamento/error")
    public ModelAndView paymentError(@RequestParam(required = false) String message) {
        logger.error("Errore generico nel pagamento: {}", message);

        ModelAndView modelAndView = new ModelAndView("cliente/payment-error");
        modelAndView.addObject("errorMessage", message != null ? message : "Si è verificato un errore durante il processo di pagamento.");
        return modelAndView;
    }

    @GetMapping("/info")
    public ResponseEntity<ClienteDTO> getClienteInfo(Authentication auth) {
        try {
            String username = JWTUtils.getUsername(auth);
            Cliente cliente = clienteService.getCliente(username);
            ClienteDTO clienteDTO = new ClienteDTO(cliente);
            clienteDTO.escape();
            return ResponseEntity.ok(clienteDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/tipo")
    public ResponseEntity<String> getTipoCliente(Authentication auth) {
        try {
            String username = JWTUtils.getUsername(auth);
            Cliente cliente = clienteService.getCliente(username);
            String tipo = cliente.getClass().getAnnotation(DiscriminatorValue.class).value();
            return ResponseEntity.ok(tipo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
