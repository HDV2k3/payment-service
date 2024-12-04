package com.example.payment.services.serviceImpl;

import com.example.payment.controller.dto.reponse.TransactionResponse;
import com.example.payment.controller.dto.request.TransactionRequest;
import com.example.payment.services.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    @Override
    public TransactionResponse created(TransactionRequest request) {
        return null;
    }
}
