package com.ecommerce.paymentservice.entity;

import com.ecommerce.paymentservice.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_id", unique = true, nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String method; // CARD, UPI, FAIL, etc.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private String transactionId;

    private String message;

    private LocalDateTime createdAt;

    // Automatically set timestamp
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}