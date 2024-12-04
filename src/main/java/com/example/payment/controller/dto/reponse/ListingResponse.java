package com.example.payment.controller.dto.reponse;

import com.example.payment.repositories.client.model.response.RoomSalePostResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListingResponse {
    String id;
    String userId;
    RoomSalePostResponse roomSalePostResponse;
    String serviceId;
    String status;
    Instant createDate;
    Instant lastModifiedDate;
    String createdBy;
    String modifiedBy;
}
