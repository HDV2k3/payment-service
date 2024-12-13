package com.example.payment.configuration.vnpay;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "vnpay")
public class VnPayVariable {
    private String url;
    private String returnUrl;
    private String tmnCode;
    private String hashSecret;
    private String apiUrl;
    private String orderInfo;
    private String baseUrl;
    private String success;
    private String error;
}
