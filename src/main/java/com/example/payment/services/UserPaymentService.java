package com.example.payment.services;

import com.example.payment.controller.dto.reponse.UserPaymentResponse;
import com.example.payment.controller.dto.request.UserPaymentRequest;

public interface UserPaymentService {
    UserPaymentResponse created(int userId);
    UserPaymentResponse getUserPayment();
    UserPaymentResponse updateUserPayment(UserPaymentRequest request);
    void updateUserBalance(String token);
    void minusBalance(int type,String roomId);
}
