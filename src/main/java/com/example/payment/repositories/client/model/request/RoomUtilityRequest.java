package com.example.payment.repositories.client.model.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public  class RoomUtilityRequest {
    Map<String, Boolean> furnitureAvailability;
    Map<String, Boolean> amenitiesAvailability;
}