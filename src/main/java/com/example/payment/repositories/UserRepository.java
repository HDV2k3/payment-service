package com.example.payment.repositories;

import com.example.payment.controller.dto.reponse.GenericApiResponse;
import com.example.payment.repositories.client.UserClient;
import com.example.payment.repositories.client.model.response.UserResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRepository {
    UserClient userClient;
    public UserResponse getUser(int userId) {
        UserResponse result = null;
        GenericApiResponse<UserResponse> clientResponse = userClient.getUser(userId);
        if (ObjectUtils.isNotEmpty(clientResponse)) {
            result = clientResponse.getData();
        }
        return result;
    }
    public UserResponse getMyInfo() {
        UserResponse result = null;
        GenericApiResponse<UserResponse> clientResponse = userClient.getMyInfo();

        if (ObjectUtils.isNotEmpty(clientResponse)) {
            result = clientResponse.getData();
        }
        return result;
    }
}
