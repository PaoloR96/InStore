package com.howtodoinjava.app.applicationcore.entity;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

public class PaymentRequest {

    @NotNull(message = "L'importo è obbligatorio")
    @DecimalMin(value = "0.01", message = "L'importo deve essere maggiore di 0")
    @Digits(integer = 10, fraction = 2, message = "Formato importo non valido")
    private BigDecimal total;

    @Pattern(regexp = "^(EUR|USD|GBP)$", message = "Valuta non supportata")
    private String currency;

    @Pattern(regexp = "^(paypal|credit_card)$", message = "Metodo di pagamento non valido")
    private String method;

    @Pattern(regexp = "^(sale|authorize|order)$", message = "Intent non valido")
    private String intent;

    @NotBlank(message = "La descrizione è obbligatoria")
    @Size(max = 255, message = "La descrizione non può superare i 255 caratteri")
    private String description;

    @URL(message = "URL di successo non valido")
    private String successUrl;

    @URL(message = "URL di cancellazione non valido")
    private String cancelUrl;

    // Campi opzionali per informazioni aggiuntive
    private String customerName;
    private String customerEmail;
    private String orderId;

    // Costruttori
    public PaymentRequest() {}

    public PaymentRequest(BigDecimal total, String currency, String description) {
        this.total = total;
        this.currency = currency;
        this.description = description;
    }

    // Getters e Setters
    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "total=" + total +
                ", currency='" + currency + '\'' +
                ", method='" + method + '\'' +
                ", intent='" + intent + '\'' +
                ", description='" + description + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}