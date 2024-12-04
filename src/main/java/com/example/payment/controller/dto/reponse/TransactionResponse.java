package com.example.payment.controller.dto.reponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionResponse {
    String id;
    String userId;
    String type;
    Double amount;
    String status;
    String method;
    String referenceId;
    Instant createDate;
    Instant lastModifiedDate;
    String createdBy;
    String modifiedBy;
}
