package com.example.payment.repositories.client.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomSalePostResponse {
    String id;
    String roomId;
    String title;
    String description;
    RoomInfoResponse roomInfo;
    RoomUtilityResponse roomUtility;
    PricingDetailsResponse pricingDetails;
    Instant availableFromDate;
    String contactInfo;
    String additionalDetails;
    String status;
    Instant createdDate;
    Instant lastModifiedDate;
    String createdBy;
    String modifiedBy;
    BigDecimal fixPrice;
    String created;
    String statusShow;
}
