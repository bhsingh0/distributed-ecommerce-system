package com.ecommerce.orderservice.service.mapper;

import com.ecommerce.orderservice.dto.OrderResponse;
import com.ecommerce.orderservice.entity.Order;
import org.mapstruct.Mapper;import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "status", expression = "java(order.getStatus().name())")
    @Mapping(target = "paymentMethod", expression = "java(order.getPaymentMethod().name())")
    OrderResponse mapToOrderResponse(Order order);
}