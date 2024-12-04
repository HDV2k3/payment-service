package com.example.payment.repositories.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class BaseEntity {

    @CreatedDate
    @Field("createdDate")
    Instant createdDate; // Automatically populated by the persistence framework

    @LastModifiedDate
    @Field("lastModifiedDate")
    Instant lastModifiedDate; // Automatically updated by the persistence framework

    @CreatedBy
    @Field("createdBy")
    String createdBy; // Automatically populated by the persistence framework

    @LastModifiedBy
    @Field("modifiedBy")
    String modifiedBy; // Automatically updated by the persistence framework
}
