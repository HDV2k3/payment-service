package com.example.payment.mappers;

import com.example.payment.controller.dto.reponse.ListingResponse;
import com.example.payment.controller.dto.request.ListingRequest;
import com.example.payment.repositories.entity.ListingsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ListingMapper {
    ListingResponse toResponseListing(ListingsEntity entity);
    ListingsEntity toCreateListing(ListingRequest request);
}
