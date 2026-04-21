package com.ecommerce.paymentservice.service;

import com.ecommerce.paymentservice.dto.PaymentRequest;
import com.ecommerce.paymentservice.dto.PaymentResponse;
import com.ecommerce.paymentservice.entity.Payment;
import com.ecommerce.paymentservice.enums.PaymentStatus;
import com.ecommerce.paymentservice.repository.PaymentRepository;
import com.ecommerce.paymentservice.service.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentResponse processPayment(PaymentRequest request) {
        Optional<Payment> existing = paymentRepository.findByOrderId(request.orderId());
        if (existing.isPresent()) {
            return paymentMapper.toResponse(existing.get());
        }

        PaymentStatus status;
        String message;
        String transactionId = null;

        if ("FAIL".equalsIgnoreCase(request.method())) {
            status = PaymentStatus.FAILED;
            message = "Mock payment failure";
        } else {
            status = PaymentStatus.SUCCESS;
            transactionId = "TXN-" + UUID.randomUUID();
            message = "Payment processed successfully";
        }

        Payment payment = Payment.builder()
                .orderId(request.orderId())
                .amount(request.amount())
                .method(request.method())
                .status(status)
                .transactionId(transactionId)
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();

        Payment saved = paymentRepository.save(payment);
        return paymentMapper.toResponse(saved);
    }

    public PaymentResponse getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return paymentMapper.toResponse(payment);
    }
}