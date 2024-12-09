package com.example.payment.controller;

import com.example.payment.controller.dto.reponse.GenericApiResponse;
import com.example.payment.controller.dto.reponse.UserPaymentResponse;
import com.example.payment.controller.dto.request.UserPaymentRequest;
import com.example.payment.facade.UserPaymentFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserPayment Controller", description = "API để quản lý User Payment.")
@Slf4j
@RestController
@RequestMapping("/userPayment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "BearerAuth") // Áp dụng xác thực token
public class UserPaymentController {
    UserPaymentFacade userPaymentFacade;
    @Operation(
            summary = "Lấy cap nhap  thanh toán của người dùng",
            description = "API này trả về thông tin thanh toán hiện tại của người dùng.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    @PutMapping("/update")
    public GenericApiResponse<UserPaymentResponse> updateUserPayment(UserPaymentRequest request)
    {
        return GenericApiResponse.success(userPaymentFacade.updateUserPayment(request));
    }

    @GetMapping("/create")
    public GenericApiResponse<UserPaymentResponse> created(@PathVariable(name = "userId") int userId) {
        var userPayment = userPaymentFacade.created(userId);
        return GenericApiResponse.success(userPayment);
    }

    @Operation(
            summary = "Lấy thông tin thanh toán của người dùng",
            description = "API này trả về thông tin thanh toán hiện tại của người dùng.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping("/getUserPayment")
    public GenericApiResponse<UserPaymentResponse> getUserPayment() {
        return GenericApiResponse.success(userPaymentFacade.getUserPayment());
    }

}
