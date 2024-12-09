package com.example.payment.controller;

import com.example.payment.controller.dto.reponse.GenericApiResponse;
import com.example.payment.facade.MomoFacade;
import com.example.payment.services.UserPaymentService;
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

@Tag(name = "MOMO Controller", description = "API để quản lý MOMO Payment.")
@Slf4j
@RestController
@RequestMapping("/momo")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MomoController {
    MomoFacade momoFacade;
    @Operation(
            summary = "Tạo đơn hàng MOMO",
            description = "API này tạo một đơn hàng mới với thông tin đơn hàng và số tiền được cung cấp, và trả về URL để thanh toán trên MOMO."
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
    @PostMapping("/payment")
    public GenericApiResponse<String> paymentWithMomo(@RequestParam String amount) {
        try {
            return GenericApiResponse.success(momoFacade.paymentWithMomo(amount));
        } catch (Exception e) {
            return GenericApiResponse.error("Failed to process payment return");
        }
    }

}
