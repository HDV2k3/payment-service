package com.example.payment.services;

import com.example.payment.controller.dto.reponse.TransactionResponse;
import com.example.payment.controller.dto.request.TransactionRequest;

public interface TransactionService {
    TransactionResponse created(TransactionRequest request);
}
