package com.example.payment.repositories.entity;

import jakarta.annotation.PostConstruct;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "listings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListingsEntity extends BaseEntity{

    @Id
    String id;
    @Field(name = "userId")
    String userId;
    @Field(name = "serviceId")
    String serviceId;
    @Field(name = "status")
    String status;
    @PostConstruct
    public void initializeCreationDetails() {
        Instant now = Instant.now();
        if (this.getCreatedDate() == null) {
            this.setCreatedDate(now);
        }
    }
}
