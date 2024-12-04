package com.example.payment.controller.dto.reponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceResponse {
    String id;
    String name;
    String description;
    Double price;
    int duration;
    Instant createDate;
    Instant lastModifiedDate;
    String createdBy;
    String modifiedBy;
}
