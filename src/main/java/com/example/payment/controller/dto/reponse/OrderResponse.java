package com.example.payment.controller.dto.reponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    private String id;
    private String transactionToken;
    private String orderIdMomo;
    private String method;
    private Double amount;
    private Integer userId;
    private String status;
    private String token;
    Instant createDate;
    Instant lastModifiedDate;
    String createdBy;
    String modifiedBy;
}
