package com.example.payment.services.serviceImpl;

import com.example.payment.configuration.momo.MoMoSecurity;
import com.example.payment.services.MomoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MomoServiceImpl implements MomoService {
    MoMoSecurity moMoSecurity;
    @Override
    public String paymentWithMomo(String amount) {
        try {
            // Constants and configuration
            String endpoint = "https://test-payment.momo.vn/gw_payment/transactionProcessor";
            String partnerCode = "MOMOOJOI20210710";
            String accessKey = "iPXneGmrJH0G8FOP";
            String secretKey = "sFcbSGRSJjwGxwhhcEktCHWYUuTuPNDB";
            String orderInfo = "NextLife";
            String returnUrl = "https://localhost:44375/Cart/Success";
            String notifyUrl = "https://localhost:44375/Cart/SavePayment";
            String extraData = "";

            // Generate unique IDs
            String orderId = String.valueOf(Instant.now().toEpochMilli());
            String requestId = String.valueOf(Instant.now().toEpochMilli());

            // Build raw hash for signature
            String rawHash = "partnerCode=" + partnerCode +
                    "&accessKey=" + accessKey +
                    "&requestId=" + requestId +
                    "&amount=" + amount +
                    "&orderId=" + orderId +
                    "&orderInfo=" + orderInfo +
                    "&returnUrl=" + returnUrl +
                    "&notifyUrl=" + notifyUrl +
                    "&extraData=" + extraData;

            // Sign the raw hash using HMAC SHA256
            String signature = moMoSecurity.signSHA256(rawHash, secretKey);

            // Build JSON request body
            JSONObject message = new JSONObject();
            message.put("partnerCode", partnerCode);
            message.put("accessKey", accessKey);
            message.put("requestId", requestId);
            message.put("amount", amount);
            message.put("orderId", orderId);
            message.put("orderInfo", orderInfo);
            message.put("returnUrl", returnUrl);
            message.put("notifyUrl", notifyUrl);
            message.put("extraData", extraData);
            message.put("requestType", "captureMoMoWallet");
            message.put("signature", signature);

            // Send payment request
            String responseFromMomo = sendPaymentRequest(endpoint, message.toString());

            // Parse response
            JSONObject response = new JSONObject(responseFromMomo);
            if (response.has("payUrl")) {
                String payUrl = response.getString("payUrl");

                // Simulate saving invoice (you should integrate with your database here)
//                saveInvoice(fullName, address, phoneNumber, note, amount);

                // Redirect to payment URL
                return payUrl;
            } else {
                throw new RuntimeException("Failed to get payment URL from MoMo response.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error initiating MoMo payment: " + e.getMessage(), e);
        }
    }
    private String sendPaymentRequest(String url, String jsonBody) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(jsonBody));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                } else {
                    throw new RuntimeException("Failed to send request to MoMo. HTTP code: " + statusCode);
                }
            }
        }
    }
//    private void saveInvoice(String fullName, String address, String phoneNumber, String note, String amount) {
//        System.out.println("Invoice saved:");
//        System.out.println("Customer: " + fullName);
//        System.out.println("Address: " + address);
//        System.out.println("Phone: " + phoneNumber);
//        System.out.println("Amount: " + amount);
//        System.out.println("Note: " + note);
//    }
}
