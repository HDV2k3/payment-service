package com.example.payment.services;

import com.example.payment.controller.dto.reponse.ListingResponse;
import com.example.payment.controller.dto.request.ListingRequest;

public interface ListingService {
    ListingResponse created(ListingRequest request);
}
