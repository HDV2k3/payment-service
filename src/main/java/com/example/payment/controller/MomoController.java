package com.example.payment.controller;
import com.example.payment.controller.dto.reponse.GenericApiResponse;
import com.example.payment.facade.MomoFacade;
import com.example.payment.services.UserPaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "MOMO Controller", description = "API để quản lý MOMO Payment.")
@Slf4j
@RestController
@RequestMapping("/momo")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MomoController {
    MomoFacade momoFacade;
    UserPaymentService userPaymentService;

    @PostMapping("/payment")
    public GenericApiResponse<String> paymentWithMomo(@RequestParam String amount, @RequestParam String token, HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            session.setAttribute("Authorization", token);
            return GenericApiResponse.success(momoFacade.paymentWithMomo(amount, token));
        } catch (Exception e) {
            log.error("Error processing payment return: ", e);
            return GenericApiResponse.error("Failed to process payment return");
        }
    }

}
