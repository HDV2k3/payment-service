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

    public UserPaymentResponse created(int userId) {
        return userPaymentService.created(userId);
    }

    public UserPaymentResponse getUserPayment() {
        return userPaymentService.getUserPayment();
    }

    public UserPaymentResponse updateUserPayment(UserPaymentRequest request) {
        return userPaymentService.updateUserPayment(request);
    }

    public  String minusBalance(int type,String roomId)
    {
        userPaymentService.minusBalance(type,roomId);
        return "Success";
    }
}
