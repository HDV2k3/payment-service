package com.example.payment.facade;

import com.example.payment.services.MobilePaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MobilePaymentFacade {
    MobilePaymentService mobilePaymentService;

   public String paymentWithMobile(String amount,String method)
   {
       return mobilePaymentService.paymentWithMobile(amount,method);
   }
}
