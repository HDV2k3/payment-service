package com.example.payment.controller.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionRequest {
    String userId;
    String type;
    Double amount;
    String status;
    String method;
    String referenceId;
}
