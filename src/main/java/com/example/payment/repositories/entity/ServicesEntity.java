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
@Document(collection = "services")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServicesEntity extends BaseEntity{
    @Id
    String id;
    @Field(name = "name")
    String name;
    @Field(name = "description")
    String description;
    @Field(name = "price")
    Double price;
    @Field(name = "duration")
    int duration;
    @PostConstruct
    public void initializeCreationDetails() {
        Instant now = Instant.now();
        if (this.getCreatedDate() == null) {
            this.setCreatedDate(now);
        }
    }
}
