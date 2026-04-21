package com.ecommerce.orderservice.dto;

import com.ecommerce.orderservice.entity.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


import java.math.BigDecimal;

public record CreateOrderRequest(

        @NotNull (message = "Product id is required")
        Long productId,

        @NotBlank(message = "Product name is required")
        String productName,

        @NotNull(message = "Unit price is required")
        @DecimalMin(value = "0.01", message = "Unit price must be greater than 0")
        BigDecimal unitPrice,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than 0")
        Integer quantity,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod
) {
}