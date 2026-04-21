package com.ecommerce.orderservice.dto;

import java.math.BigDecimal;

public record PaymentServiceRequest(
        Long orderId,
        BigDecimal amount,
        String method
) {
}