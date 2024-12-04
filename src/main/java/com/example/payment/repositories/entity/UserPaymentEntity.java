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
@Document(collection = "users_payment")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPaymentEntity extends BaseEntity{
        @Id
    String id;
        @Field(name = "userId")
        int userId;
        @Field(name = "balance")
    Double balance;
    @PostConstruct
    public void initializeCreationDetails() {
        Instant now = Instant.now();
        if (this.getCreatedDate() == null || this.getLastModifiedDate()==null) {
            this.setCreatedDate(now);
            this.setLastModifiedDate(now);
        }
    }
}
