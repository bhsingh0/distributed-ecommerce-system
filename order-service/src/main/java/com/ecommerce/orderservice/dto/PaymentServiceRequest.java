package com.ecommerce.orderservice.dto;

import java.math.BigDecimal;

public record PaymentServiceRequest(
        Long orderId,
        String orderCode,
        BigDecimal amount,
        String method
) {
}