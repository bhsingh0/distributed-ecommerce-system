package com.ecommerce.orderservice.config;

import com.ecommerce.orderservice.exception.NonRetryablePaymentException;
import com.ecommerce.orderservice.exception.RetryablePaymentException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {


    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {

            case 400 -> new NonRetryablePaymentException("Bad request");

            case 404 -> new NonRetryablePaymentException("Resource not found");

            case 500 -> new RetryablePaymentException("Internal server error");

            default -> new RetryablePaymentException("Unexpected error: " + response.status());
        };
    }
}