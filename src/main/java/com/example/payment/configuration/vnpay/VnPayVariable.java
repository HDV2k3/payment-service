package com.example.payment.configuration.vnpay;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "vnpay")
public class VnPayVariable {
    private String VNP_PAY_URL;
    private String VNP_RETURN_URL;
    private String VNP_TMN_CODE;
    private String VNP_HASH_SECRET;
    private String VNP_API_URL;
    private String ORDER_INFO;
    private String BASE_URL;
    private String ERROR;
    private String SUCCESS;
}
