package com.example.payment.services;

import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {
     String createOrder(HttpServletRequest request, String token,int amount, String orderInfor, String urlReturn);
     int orderReturn(HttpServletRequest request);
}
