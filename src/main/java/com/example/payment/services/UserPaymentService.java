package com.example.payment.services;

import com.example.payment.controller.dto.reponse.UserPaymentResponse;
import com.example.payment.controller.dto.request.UserPaymentRequest;
import com.example.payment.repositories.entity.UserPaymentEntity;

public interface UserPaymentService {
    UserPaymentResponse created(int userId);
    UserPaymentResponse getUserPayment();
    UserPaymentResponse updateUserPayment(UserPaymentRequest request);
    UserPaymentEntity updateUserBalance(String token);
}
