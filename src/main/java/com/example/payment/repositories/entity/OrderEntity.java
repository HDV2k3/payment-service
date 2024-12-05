package com.example.payment.repositories.entity;

import jakarta.annotation.PostConstruct;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)

public class OrderEntity extends BaseEntity{
    @Id
    private String id;
    private String transactionToken;
    private String orderIdMomo;
    private String method;
    private Double amount;
    private Integer userId;
    private String status;
    private String token;
    @PostConstruct
    public void initializeCreationDetails() {
        Instant now = Instant.now();
        if (this.getCreatedDate() == null) {
            this.setCreatedDate(now);
        }
    }

}