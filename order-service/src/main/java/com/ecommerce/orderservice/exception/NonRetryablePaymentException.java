package com.ecommerce.orderservice.exception;

public class NonRetryablePaymentException extends RuntimeException {
    public NonRetryablePaymentException(String message) {
        super(message);
    }
}