package com.ecommerce.paymentservice.controller;

import com.ecommerce.paymentservice.dto.PaymentRequest;
import com.ecommerce.paymentservice.dto.PaymentResponse;

import com.ecommerce.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    @PostMapping("/process")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {

        MDC.put("orderId", String.valueOf(request.orderId()));
        MDC.put("orderCode", request.orderCode());
        try {
            log.info("Payment request received");

            PaymentResponse response = paymentService.processPayment(request);

            log.info("Payment processed successfully: status={}, transactionId={}",
                    response.status(),
                    response.transactionId());

            return ResponseEntity.ok(response);

        } finally {
            MDC.remove("orderId");
            MDC.remove("orderCode");
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }
}