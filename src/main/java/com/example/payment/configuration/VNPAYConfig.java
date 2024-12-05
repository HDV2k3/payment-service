package com.example.payment.configuration;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class VNPAYConfig {

    // Configuration constants
    public static final String VNP_PAY_URL = EnvConfig.get("VNP_PAY_URL");
    public static final String VNP_RETURN_URL = EnvConfig.get("VNP_RETURN_URL");
    public static final String VNP_TMN_CODE = EnvConfig.get("VNP_TMN_CODE");
    public static final String VNP_HASH_SECRET = EnvConfig.get("VNP_HASH_SECRET");

    /**
     * Hashes all fields in the provided map using HMAC SHA-512.
     *
     * @param fields The map of fields to hash.
     * @return The HMAC SHA-512 hash of the fields.
     */
    public static String hashAllFields(Map<String, String> fields) {
        // Sort field names
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        // Build the raw data string
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = fields.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                sb.append(fieldName).append("=").append(fieldValue);
                if (i < fieldNames.size() - 1) {
                    sb.append("&");
                }
            }
        }

        // Hash the resulting string with HMAC SHA-512
        return hmacSHA512(VNP_HASH_SECRET, sb.toString());
    }

    /**
     * Creates an HMAC SHA-512 hash for the given data using the provided key.
     *
     * @param key  The secret key.
     * @param data The data to hash.
     * @return The resulting HMAC SHA-512 hash in hexadecimal format.
     */
    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new IllegalArgumentException("Key and data must not be null.");
            }

            // Initialize HMAC SHA-512
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes(StandardCharsets.UTF_8);
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);

            // Perform hashing
            byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(result.length * 2);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error while generating HMAC SHA-512 hash: " + ex.getMessage(), ex);
        }
    }

    /**
     * Retrieves the IP address of the client from the HTTP request.
     *
     * @param request The HTTP request.
     * @return The client's IP address.
     */
    public static String getIpAddress(HttpServletRequest request) {
        try {
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null || ipAddress.isEmpty()) {
                ipAddress = request.getLocalAddr();
            }
            return ipAddress;
        } catch (Exception e) {
            return "Invalid IP: " + e.getMessage();
        }
    }

    /**
     * Generates a random numeric string of the specified length.
     *
     * @param length The desired length of the random number.
     * @return A random numeric string.
     */
    public static String getRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // Generate a digit (0-9)
        }
        return sb.toString();
    }
}
