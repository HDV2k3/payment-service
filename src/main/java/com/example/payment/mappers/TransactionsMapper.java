package com.example.payment.mappers;

import com.example.payment.controller.dto.reponse.TransactionResponse;
import com.example.payment.controller.dto.request.TransactionRequest;
import com.example.payment.repositories.entity.TransactionsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionsMapper {
    TransactionsEntity toCreateTransaction(TransactionRequest request);
    TransactionResponse toResponseTransaction(TransactionsEntity entity);
}
