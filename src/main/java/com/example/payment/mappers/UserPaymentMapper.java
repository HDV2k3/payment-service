package com.example.payment.mappers;

import com.example.payment.controller.dto.reponse.UserPaymentResponse;
import com.example.payment.controller.dto.request.UserPaymentRequest;
import com.example.payment.repositories.entity.UserPaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface UserPaymentMapper {

    UserPaymentEntity toCreateUserPayment(UserPaymentRequest request);

    UserPaymentResponse toResponse(UserPaymentEntity entity);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId" ,ignore = true)
    void updateUserPayment(UserPaymentRequest request, @MappingTarget UserPaymentEntity entity);

}
