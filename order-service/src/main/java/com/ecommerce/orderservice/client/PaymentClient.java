package com.ecommerce.orderservice.client;

import com.ecommerce.orderservice.dto.PaymentServiceRequest;
import com.ecommerce.orderservice.dto.PaymentServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "payment-service",
        url = "http://localhost:8085"
)
public interface PaymentClient {

    @PostMapping("/api/payments/process")
    PaymentServiceResponse processPayment(
            @RequestHeader("Authorization") String authorization,
            @RequestBody PaymentServiceRequest request
    );
}