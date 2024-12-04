package com.example.payment.repositories.client.model.request;

import com.example.payment.utility.CustomInstantDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public  class RoomInfoRequest {
    String name;
    String description;
    String address;
    String type;
    String style;
    String floor;
    Double width;
    Double height;
    Double totalArea;
    Integer capacity;
    Integer numberOfBedrooms;
    Integer numberOfBathrooms;
    @JsonDeserialize(using = CustomInstantDeserializer.class)
    Instant availableFromDate;
}