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
@Document(collection = "transactions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionsEntity extends BaseEntity{
    @Id
    String id;
    @Field(name = "userId")
    int userId;
    @Field(name = "type")
    String type;
    @Field(name = "amount")
    Double amount;
    @Field(name = "status")
    String status;
    @Field(name = "method")
    String method;
    @Field(name = "referenceId")
    String referenceId;
    @PostConstruct
    public void initializeCreationDetails() {
        Instant now = Instant.now();
        if (this.getCreatedDate() == null) {
            this.setCreatedDate(now);
        }
    }
}
