package com.example.payment.mappers;

import com.example.payment.controller.dto.reponse.OrderResponse;
import com.example.payment.repositories.entity.OrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toOrderResponse(OrderEntity orderEntity);
}
