package com.example.payment.facade;

import com.example.payment.controller.dto.reponse.GenericApiResponse;
import com.example.payment.controller.dto.reponse.OrderResponse;
import com.example.payment.controller.dto.reponse.PageResponse;
import com.example.payment.services.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderFacade {
    OrderService orderService;
    public PageResponse<OrderResponse> getAllOrderByUser(int userId, int page, int size)
    {
       return orderService.getAllByUser(userId, page, size);
    }
}
