package com.example.payment.services;

import com.example.payment.controller.dto.reponse.OrderResponse;
import com.example.payment.controller.dto.reponse.PageResponse;

public interface OrderService {
    PageResponse<OrderResponse> getAllByUser(int userId,int page,int size);
}
