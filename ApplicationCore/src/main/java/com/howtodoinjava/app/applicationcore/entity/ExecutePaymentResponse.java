package com.howtodoinjava.app.applicationcore.entity;

public class ExecutePaymentResponse {
    private String paymentId;
    private String state;
    private String payerId;
    private String transactionId;

    // Costruttori
    public ExecutePaymentResponse() {}

    public ExecutePaymentResponse(String paymentId, String state, String payerId, String transactionId) {
        this.paymentId = paymentId;
        this.state = state;
        this.payerId = payerId;
        this.transactionId = transactionId;
    }

    // Getter e Setter
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}