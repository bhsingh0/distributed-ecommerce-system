package com.ecommerce.orderservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        Long productId,
        String productName,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal totalAmount,
        String status,
        String paymentMethod,
        LocalDateTime createdAt,
        String paymentTransactionId
) {
}