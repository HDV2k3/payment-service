package com.example.payment.controller.dto.reponse;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;


import java.time.Instant;
import java.util.LinkedList;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseIndexResponse {
    String id;
    String roomId;
    Integer index;
    private Instant expiry;
    LinkedList<Integer> types = new LinkedList<>();


    Instant createdDate; // Automatically populated by the persistence framework


    Instant lastModifiedDate; // Automatically updated by the persistence framework

    @CreatedBy
    String createdBy; // Automatically populated by the persistence framework


    String modifiedBy; // Automatically updated by the persistence framework


    int userId;
}
