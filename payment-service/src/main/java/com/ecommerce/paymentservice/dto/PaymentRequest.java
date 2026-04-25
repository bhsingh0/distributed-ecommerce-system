package com.ecommerce.paymentservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequest(

        @NotNull(message = "Order id is required")
        Long orderId,

        @NotBlank(message = "Order code is required")
        String orderCode,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.1", message = "Amount must be greater than 0")
        BigDecimal amount,

        @NotBlank(message = "Payment method is required")
        String method
) {
}