package com.ecommerce.orderservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentServiceResponse(
        UUID paymentId,
        Long orderId,
        BigDecimal amount,
        String method,
        String status,
        String transactionId,
        String message
) {
}