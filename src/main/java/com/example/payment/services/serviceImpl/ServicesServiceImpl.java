package com.example.payment.services.serviceImpl;

import com.example.payment.controller.dto.reponse.ServiceResponse;
import com.example.payment.controller.dto.request.ServicesRequest;
import com.example.payment.services.ServicesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ServicesServiceImpl implements ServicesService {
    @Override
    public ServiceResponse created(ServicesRequest request) {
        return null;
    }
}
