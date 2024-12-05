package com.example.payment.controller;

import com.example.payment.controller.dto.reponse.GenericApiResponse;
import com.example.payment.facade.MobilePaymentFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payment Mobile Controller", description = "API để quản lý Update Payment With Mobile app.")
@Slf4j
@RestController
@RequestMapping("/mobile")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MobilePaymentController {

    MobilePaymentFacade mobilePaymentFacade;
    @Operation(
            summary = "Them va update balance for userPayment",
            description = "Them va update balance for userPayment can truyen vao 2 requestParam la amount (so tien) va method (phuong thuc)"
            , security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "URL thanh toán thành công",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "Lỗi đầu vào không hợp lệ",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Lỗi trong quá trình thanh toán",
                    content = @Content)
    })
    @PostMapping("/update/balance")
    public GenericApiResponse<String> mobilePaymentUpdate(@RequestParam String amount,@RequestParam String method)
    {
        return GenericApiResponse.success(mobilePaymentFacade.paymentWithMobile(amount,method));
    }
}
