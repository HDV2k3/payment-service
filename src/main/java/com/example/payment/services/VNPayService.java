package com.example.payment.services;

import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {
     String createOrder(HttpServletRequest request,int amount,  String urlReturn);
     String orderReturn(HttpServletRequest request);
}
