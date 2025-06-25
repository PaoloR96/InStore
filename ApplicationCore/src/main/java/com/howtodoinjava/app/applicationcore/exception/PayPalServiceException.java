package com.howtodoinjava.app.applicationcore.exception;

public class PayPalServiceException extends RuntimeException {

    private String errorCode;
    private String details;

    public PayPalServiceException(String message) {
        super(message);
    }

    public PayPalServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayPalServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public PayPalServiceException(String message, String errorCode, String details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    public PayPalServiceException(String message, Throwable cause, String errorCode, String details) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details = details;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "PayPalServiceException{" +
                "message='" + getMessage() + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}