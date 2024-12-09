package com.example.payment.configuration.momo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "momo")
public class MomoConfig {
    private String endpoint;
    private String partnerCode;
    private String accessKey;
    private String secretKey;
    private String orderInfo;
    private String returnUrl;
    private String notifyUrl;
}
