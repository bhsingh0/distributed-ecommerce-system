package com.ecommerce.orderservice.config;

import com.ecommerce.orderservice.exception.PaymentServiceException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new PaymentServiceException("Bad request to payment-service");
            case 404 -> new PaymentServiceException("Resource not found in payment-service");
            case 500 -> new PaymentServiceException("Payment service internal error");
            default -> new PaymentServiceException("Unexpected error from payment-service: " + response.status());
        };
    }
}