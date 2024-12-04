package com.example.payment.repositories.client.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class UserResponse {
    int id;
    String email;
    String firstName;
    String lastName;
    LocalDate dayOfBirth;
    String avatar;
    String verificationToken;
    boolean enabled;
    Set<RoleResponse> roles;
}
