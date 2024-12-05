package com.example.payment.facade;

import com.example.payment.services.MomoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MomoFacade {
    MomoService momoService;
    public String paymentWithMomo(String amount,String token)
    {
        return momoService.paymentWithMomo(amount,token);
    }
}
