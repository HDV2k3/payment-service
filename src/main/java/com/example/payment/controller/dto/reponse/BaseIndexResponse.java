package com.example.payment.controller.dto.reponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseIndexResponse {
    String id;
    String roomId;
    Integer index;
}
