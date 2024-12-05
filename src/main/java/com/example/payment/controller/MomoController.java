package com.example.payment.controller;

import com.example.payment.controller.dto.reponse.GenericApiResponse;
import com.example.payment.facade.MomoFacade;
import com.example.payment.services.UserPaymentService;
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
    @PostMapping("/payment")
    public GenericApiResponse<String> paymentWithMomo(@RequestParam String amount) {
        try {
            return GenericApiResponse.success(momoFacade.paymentWithMomo(amount));
        } catch (Exception e) {
            return GenericApiResponse.error("Failed to process payment return");
        }
    }

}
