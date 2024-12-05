package com.example.payment.controller;

import com.example.payment.controller.dto.reponse.GenericApiResponse;
import com.example.payment.controller.dto.reponse.OrderResponse;
import com.example.payment.controller.dto.reponse.PageResponse;
import com.example.payment.facade.OrderFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order Controller", description = "API để quản lý Order History.")
@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderFacade orderFacade;
    @GetMapping("/all")
    @Operation(summary ="lay tat ca lich su nap tien cua user",security = {@SecurityRequirement(name = "bearerAuth")})
    public GenericApiResponse<PageResponse<OrderResponse>> getAllOrderByUser(
            @RequestParam(value = "userId") int userId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        var result = orderFacade.getAllOrderByUser(userId, page, size);
        return GenericApiResponse.success(result);
    }
}
