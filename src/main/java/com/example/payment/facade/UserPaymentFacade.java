package com.example.payment.facade;

import com.example.payment.controller.dto.reponse.UserPaymentResponse;
import com.example.payment.controller.dto.request.UserPaymentRequest;
import com.example.payment.services.UserPaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserPaymentFacade {

    UserPaymentService userPaymentService;
    public UserPaymentResponse created(UserPaymentRequest request)
    {
        return userPaymentService.created(request);
    }

    public UserPaymentResponse getUserPayment()
    {
        return userPaymentService.getUserPayment();
    }
}
