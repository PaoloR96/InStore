package com.howtodoinjava.app.applicationcore.entity;

// PaymentResponse.java
public class PaymentResponse {
    private String paymentId;
    private String state;
    private String approvalUrl;

    // Costruttori
    public PaymentResponse() {}

    public PaymentResponse(String paymentId, String state, String approvalUrl) {
        this.paymentId = paymentId;
        this.state = state;
        this.approvalUrl = approvalUrl;
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

    public String getApprovalUrl() {
        return approvalUrl;
    }

    public void setApprovalUrl(String approvalUrl) {
        this.approvalUrl = approvalUrl;
    }
}
