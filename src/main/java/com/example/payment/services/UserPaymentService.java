package com.example.payment.services;

import com.example.payment.controller.dto.reponse.UserPaymentResponse;
import com.example.payment.controller.dto.request.UserPaymentRequest;

public interface UserPaymentService {

    UserPaymentResponse created(UserPaymentRequest request);
    UserPaymentResponse getUserPayment();
}
