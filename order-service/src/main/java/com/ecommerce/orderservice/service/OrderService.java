package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.client.PaymentClient;
import com.ecommerce.orderservice.dto.CreateOrderRequest;
import com.ecommerce.orderservice.dto.OrderResponse;
import com.ecommerce.orderservice.dto.PaymentServiceRequest;
import com.ecommerce.orderservice.dto.PaymentServiceResponse;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderStatus;
import com.ecommerce.orderservice.exception.ResourceNotFoundException;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.service.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentClient paymentClient;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository,
                        PaymentClient paymentClient,
                        OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.paymentClient = paymentClient;
        this.orderMapper = orderMapper;
    }

    public OrderResponse createOrder(CreateOrderRequest request, String authorization) {

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

        // Save first so ID is generated
        Order savedOrder = orderRepository.save(order);

        PaymentServiceRequest paymentRequest = new PaymentServiceRequest(
                savedOrder.getId(),
                savedOrder.getTotalAmount(),
                request.paymentMethod().name()
        );

        PaymentServiceResponse paymentResponse = paymentClient.processPayment(
                authorization,
                paymentRequest
        );

        if ("SUCCESS".equalsIgnoreCase(paymentResponse.status())) {
            savedOrder.setStatus(OrderStatus.CONFIRMED);
        } else {
            savedOrder.setStatus(OrderStatus.PAYMENT_FAILED);
        }

        savedOrder.setPaymentTransactionId(paymentResponse.transactionId());

        Order updatedOrder = orderRepository.save(savedOrder);
        return orderMapper.mapToOrderResponse(updatedOrder);
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
}