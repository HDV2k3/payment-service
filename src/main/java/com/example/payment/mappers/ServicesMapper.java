package com.example.payment.mappers;

import com.example.payment.controller.dto.reponse.ServiceResponse;
import com.example.payment.controller.dto.request.ServicesRequest;
import com.example.payment.repositories.entity.ServicesEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServicesMapper {

    ServicesEntity toCreateServices(ServicesRequest request);

    ServiceResponse toResponseServices(ServicesEntity entity);
}
