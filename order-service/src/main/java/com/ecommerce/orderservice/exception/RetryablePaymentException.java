package com.ecommerce.orderservice.exception;

public class RetryablePaymentException extends RuntimeException {
    public RetryablePaymentException(String message) {
        super(message);
    }
}