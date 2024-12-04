package com.example.payment.repositories.client.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public  class RoomInfoResponse {
    String name;
    String description;
    String address;
    String type;
    String style;
    String floor;
    List<PostImageResponse> postImages;
    Double width;
    Double height;
    Double totalArea;
    Integer capacity;
    Integer numberOfBedrooms;
    Integer numberOfBathrooms;
    Instant availableFromDate;
}