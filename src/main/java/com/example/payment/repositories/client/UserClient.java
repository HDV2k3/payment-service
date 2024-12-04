package com.example.payment.repositories.client;
import com.example.payment.configuration.security.AuthenticationRequestInterceptor;
import com.example.payment.controller.dto.reponse.GenericApiResponse;
import com.example.payment.repositories.client.model.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user",url = "${service.url.user}", configuration = {AuthenticationRequestInterceptor.class})
public interface UserClient {
    @GetMapping(value = "/users/get-by-id/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    GenericApiResponse<UserResponse> getUser(@PathVariable("userId") int userId);
    @GetMapping(value = "/users/my-info",produces = MediaType.APPLICATION_JSON_VALUE)
    GenericApiResponse<UserResponse> getMyInfo();
}
