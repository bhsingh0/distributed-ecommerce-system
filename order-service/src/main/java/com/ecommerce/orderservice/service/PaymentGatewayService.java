package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.client.PaymentClient;
import com.ecommerce.orderservice.dto.PaymentServiceRequest;
import com.ecommerce.orderservice.dto.PaymentServiceResponse;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayService {

    private static final Logger log = LoggerFactory.getLogger(PaymentGatewayService.class);

    private final PaymentClient paymentClient;

    public PaymentGatewayService(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    @Retry(name = "paymentRetry", fallbackMethod = "paymentFallback")
    public PaymentServiceResponse callPayment(PaymentServiceRequest request) {
        log.info("Entered payment controller for orderId={}", request.orderId());
        return paymentClient.processPayment(request);
    }

    public PaymentServiceResponse paymentFallback(PaymentServiceRequest request, Exception ex) {
        log.error("Fallback triggered for orderId={}, reason={}", request.orderId(), ex.toString());
        return new PaymentServiceResponse(
                null,
                request.orderId(),
                request.amount(),
                request.method(),
                "FAILED",
                null,
                "Payment service unavailable after retries"
        );
    }
}