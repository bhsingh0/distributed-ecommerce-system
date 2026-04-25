package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.CreateOrderRequest;
import com.ecommerce.orderservice.dto.OrderResponse;
import com.ecommerce.orderservice.dto.PaymentServiceRequest;
import com.ecommerce.orderservice.dto.PaymentServiceResponse;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderStatus;
import com.ecommerce.orderservice.exception.ResourceNotFoundException;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.service.mapper.OrderMapper;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.MDC;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final PaymentGatewayService paymentGatewayService;
    private final OrderMapper orderMapper;
    private final Tracer tracer;

    public OrderService(OrderRepository orderRepository,
                        PaymentGatewayService paymentGatewayService,
                        OrderMapper orderMapper, Tracer tracer) {
        this.orderRepository = orderRepository;
        this.paymentGatewayService = paymentGatewayService;
        this.orderMapper = orderMapper;
        this.tracer = tracer;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        Order order = new Order();
        order.setProductId(request.productId());
        order.setProductName(request.productName());
        order.setUnitPrice(request.unitPrice());
        order.setQuantity(request.quantity());
        order.setTotalAmount(
                request.unitPrice().multiply(BigDecimal.valueOf(request.quantity()))
        );
        order.setPaymentMethod(request.paymentMethod());
        order.setStatus(OrderStatus.PENDING);

        try {
            Order savedOrder = orderRepository.save(order);

            MDC.put("orderId", String.valueOf(savedOrder.getId()));
            addOrderFieldsToSpan(savedOrder.getId(), null);

            log.info("Order persisted: id={}, productId={}", savedOrder.getId(), savedOrder.getProductId());

            savedOrder.setOrderCode(String.format("COS-%05d", savedOrder.getId()));
            savedOrder = orderRepository.save(savedOrder);

            MDC.put("orderCode", savedOrder.getOrderCode());
            addOrderFieldsToSpan(savedOrder.getId(), savedOrder.getOrderCode());

            log.info("Order code assigned: id={}, orderCode={}", savedOrder.getId(), savedOrder.getOrderCode());

            PaymentServiceRequest paymentRequest = new PaymentServiceRequest(
                    savedOrder.getId(),
                    savedOrder.getOrderCode(),
                    savedOrder.getTotalAmount(),
                    request.paymentMethod().name()
            );

            log.info("Calling payment-service");

            PaymentServiceResponse paymentResponse = paymentGatewayService.callPayment(paymentRequest);

            log.info("Payment response received: orderId={}, status={}, transactionId={}",
                    savedOrder.getId(),
                    paymentResponse.status(),
                    paymentResponse.transactionId());

            if ("SUCCESS".equalsIgnoreCase(paymentResponse.status())) {
                savedOrder.setStatus(OrderStatus.CONFIRMED);
            } else {
                savedOrder.setStatus(OrderStatus.PAYMENT_FAILED);
            }

            savedOrder.setPaymentTransactionId(paymentResponse.transactionId());

            Order updatedOrder = orderRepository.save(savedOrder);

            log.info("Final order saved: id={}, orderCode={}, status={}",
                    updatedOrder.getId(),
                    updatedOrder.getOrderCode(),
                    updatedOrder.getStatus());

            return orderMapper.mapToOrderResponse(updatedOrder);

        } finally {
            MDC.remove("orderId");
            MDC.remove("orderCode");
        }
    }
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        return orderMapper.mapToOrderResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::mapToOrderResponse)
                .toList();
    }

    private void addOrderFieldsToSpan(Long orderId, String orderCode) {
        Span span = tracer.currentSpan();
        if (span != null) {
            if (orderId != null) {
                span.tag("order.id", String.valueOf(orderId));
            }
            if (orderCode != null) {
                span.tag("order.code", orderCode);
            }
        }
    }
}