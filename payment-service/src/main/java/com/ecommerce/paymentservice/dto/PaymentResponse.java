package com.ecommerce.paymentservice.dto;

import com.ecommerce.paymentservice.enums.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentResponse(
        UUID paymentId,
        Long orderId,
        BigDecimal amount,
        String method,
        PaymentStatus status,
        String transactionId,
        String message
) {
}