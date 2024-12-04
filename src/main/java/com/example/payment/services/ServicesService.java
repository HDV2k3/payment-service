package com.example.payment.services;

import com.example.payment.controller.dto.reponse.ServiceResponse;
import com.example.payment.controller.dto.request.ServicesRequest;

public interface ServicesService {
    ServiceResponse created(ServicesRequest request);
}
