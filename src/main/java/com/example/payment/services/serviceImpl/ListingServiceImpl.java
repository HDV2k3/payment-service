package com.example.payment.services.serviceImpl;

import com.example.payment.controller.dto.reponse.ListingResponse;
import com.example.payment.controller.dto.request.ListingRequest;
import com.example.payment.services.ListingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ListingServiceImpl implements ListingService {
    @Override
    public ListingResponse created(ListingRequest request) {
        return null;
    }
}
