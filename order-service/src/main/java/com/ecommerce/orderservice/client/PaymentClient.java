package com.ecommerce.orderservice.client;

import com.ecommerce.orderservice.dto.PaymentServiceRequest;
import com.ecommerce.orderservice.dto.PaymentServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ecommerce.orderservice.config.FeignConfig;

@FeignClient(
        name = "payment-service",
        url = "${payment-service.url}",
        configuration = FeignConfig.class
)
public interface PaymentClient {

    @PostMapping("/api/payments/process")
    PaymentServiceResponse processPayment(@RequestBody PaymentServiceRequest request);
}