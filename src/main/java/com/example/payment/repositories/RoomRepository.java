package com.example.payment.repositories;

import com.example.payment.controller.dto.reponse.GenericApiResponse;
import com.example.payment.repositories.client.RoomClient;
import com.example.payment.repositories.client.model.response.RoomSalePostResponse;
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
public class RoomRepository {
    RoomClient roomClient;

    public RoomSalePostResponse getRoomById(int id) {
        RoomSalePostResponse result = null;
        GenericApiResponse<RoomSalePostResponse> clientResponse = roomClient.getRoomById(id);
        if (ObjectUtils.isNotEmpty(clientResponse)) {
            result = clientResponse.getData();
        }
        return result;
    }
}
