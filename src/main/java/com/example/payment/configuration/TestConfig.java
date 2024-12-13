package com.example.payment.configuration;

import com.example.payment.configuration.vnpay.VnPayVariable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestConfig {
    @Autowired
    private VnPayVariable vnPayVariable;

    @PostConstruct
    public void init() {
        System.out.println("vnPayVariable: " + vnPayVariable);
    }
}
