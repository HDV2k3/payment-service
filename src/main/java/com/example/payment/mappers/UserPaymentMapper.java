package com.example.payment.mappers;

import com.example.payment.controller.dto.reponse.UserPaymentResponse;
import com.example.payment.controller.dto.request.UserPaymentRequest;
import com.example.payment.repositories.entity.UserPaymentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface UserPaymentMapper {

    UserPaymentEntity toCreateUserPayment(UserPaymentRequest request);

    UserPaymentResponse toResponse(UserPaymentEntity entity);
}
