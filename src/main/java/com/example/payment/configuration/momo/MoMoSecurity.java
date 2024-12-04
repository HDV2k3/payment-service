package com.example.payment.configuration.momo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
@Slf4j
@Component
public class MoMoSecurity {


    public MoMoSecurity() {
    }

    public String getHash(String partnerCode, String merchantRefId,
                          String amount, String paymentCode, String storeId, String storeName, String publicKeyXML) {
        String json = "{\"partnerCode\":\"" +
                partnerCode + "\",\"partnerRefId\":\"" +
                merchantRefId + "\",\"amount\":" +
                amount + ",\"paymentCode\":\"" +
                paymentCode + "\",\"storeId\":\"" +
                storeId + "\",\"storeName\":\"" +
                storeName + "\"}";

        log.info("Raw hash: " + json);

        byte[] data = json.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        String result = null;
        try {
            // MoMo's public key has format PEM.
            // You must convert it to XML format. Recommend tool: https://superdry.apphb.com/tools/online-rsa-key-converter
            PublicKey publicKey = getPublicKeyFromXML(publicKeyXML);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedData = cipher.doFinal(data);
            result = Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            log.info("Error in encryption: " + e.getMessage());
        }

        return result;
    }

    public String buildQueryHash(String partnerCode, String merchantRefId,
                                 String requestId, String publicKey) {
        String json = "{\"partnerCode\":\"" +
                partnerCode + "\",\"partnerRefId\":\"" +
                merchantRefId + "\",\"requestId\":\"" +
                requestId + "\"}";

        log.info("Raw hash: " + json);

        byte[] data = json.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        String result = null;
        try {
            PublicKey pubKey = getPublicKeyFromXML(publicKey);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] encryptedData = cipher.doFinal(data);
            result = Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            log.info("Error in encryption: " + e.getMessage());
        }

        return result;
    }

    public String buildRefundHash(String partnerCode, String merchantRefId,
                                  String momoTranId, long amount, String description, String publicKey) {
        String json = "{\"partnerCode\":\"" +
                partnerCode + "\",\"partnerRefId\":\"" +
                merchantRefId + "\",\"momoTransId\":\"" +
                momoTranId + "\",\"amount\":" +
                amount + ",\"description\":\"" +
                description + "\"}";

        log.info("Raw hash: " + json);

        byte[] data = json.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        String result = null;
        try {
            PublicKey pubKey = getPublicKeyFromXML(publicKey);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] encryptedData = cipher.doFinal(data);
            result = Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            log.info("Error in encryption: " + e.getMessage());
        }

        return result;
    }

    public String signSHA256(String message, String key) {
        try {
            byte[] keyByte = key.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            byte[] messageBytes = message.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            Mac hmacsha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(keyByte, "HmacSHA256");
            hmacsha256.init(secretKey);
            byte[] hashmessage = hmacsha256.doFinal(messageBytes);
            StringBuilder hex = new StringBuilder();
            for (byte b : hashmessage) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            log.info("Error in SHA256 signing: " + e.getMessage());
            return null;
        }
    }

    private PublicKey getPublicKeyFromXML(String publicKeyXML) throws Exception {
        // Convert XML to PublicKey (You need to implement this if required)
        // You can use PEMReader to load the public key, or another appropriate method to convert XML to PublicKey
        // For simplicity, we're using a hardcoded method here:
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyXML));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}
