package com.howtodoinjava.app.applicationcore.service;

import com.howtodoinjava.app.applicationcore.entity.ExecutePaymentResponse;
import com.howtodoinjava.app.applicationcore.entity.PaymentRequest;
import com.howtodoinjava.app.applicationcore.entity.PaymentResponse;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalService {

    @Autowired
    private APIContext apiContext;

    public PaymentResponse createPayment(PaymentRequest paymentRequest) throws PayPalRESTException {

        // Crea l'oggetto Amount
        Amount amount = new Amount();
        amount.setCurrency(paymentRequest.getCurrency());
        amount.setTotal(paymentRequest.getTotal().toString());

        // Crea la transazione
        Transaction transaction = new Transaction();
        transaction.setDescription("Pagamento per ordine");
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // Crea il payer
        Payer payer = new Payer();
        payer.setPaymentMethod(paymentRequest.getMethod());

        // Crea il payment
        Payment payment = new Payment();
        payment.setIntent(paymentRequest.getIntent());
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        // Crea le redirect URLs
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(paymentRequest.getCancelUrl());
        redirectUrls.setReturnUrl(paymentRequest.getSuccessUrl());
        payment.setRedirectUrls(redirectUrls);

        // Crea il pagamento
        Payment createdPayment = payment.create(apiContext);

        // Estrai l'URL di approvazione
        String approvalUrl = null;
        for (Links link : createdPayment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                approvalUrl = link.getHref();
                break;
            }
        }

        // Crea la risposta
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(createdPayment.getId());
        response.setState(createdPayment.getState());
        response.setApprovalUrl(approvalUrl);

        return response;
    }

    public String getApprovalUrl(String paymentId) throws PayPalRESTException {
        Payment payment = Payment.get(apiContext, paymentId);

        for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                return link.getHref();
            }
        }

        throw new RuntimeException("URL di approvazione non trovato per il pagamento: " + paymentId);
    }

    public ExecutePaymentResponse executePayment(String paymentId, String payerId)
            throws PayPalRESTException {

        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executedPayment = payment.execute(apiContext, paymentExecution);

        // Crea la risposta
        ExecutePaymentResponse response = new ExecutePaymentResponse();
        response.setPaymentId(executedPayment.getId());
        response.setState(executedPayment.getState());
        response.setPayerId(payerId);

        // Estrai l'ID della transazione se disponibile
        if (executedPayment.getTransactions() != null &&
                !executedPayment.getTransactions().isEmpty() &&
                executedPayment.getTransactions().get(0).getRelatedResources() != null &&
                !executedPayment.getTransactions().get(0).getRelatedResources().isEmpty()) {

            RelatedResources relatedResource = executedPayment.getTransactions().get(0)
                    .getRelatedResources().get(0);
            if (relatedResource.getSale() != null) {
                response.setTransactionId(relatedResource.getSale().getId());
            }
        }

        return response;
    }
}