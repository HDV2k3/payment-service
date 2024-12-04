package com.example.payment.services;

import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {
    public String createOrder(HttpServletRequest request, int amount, String orderInfor, String urlReturn);
    public int orderReturn(HttpServletRequest request);
}
