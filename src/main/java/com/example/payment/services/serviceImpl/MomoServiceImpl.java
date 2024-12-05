package com.example.payment.services.serviceImpl;
import com.example.payment.configuration.EnvConfig;
import com.example.payment.configuration.momo.MoMoSecurity;
import com.example.payment.repositories.OrderRepository;
import com.example.payment.repositories.UserPaymentRepository;
import com.example.payment.repositories.UserRepository;
import com.example.payment.repositories.entity.OrderEntity;
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
    OrderRepository orderRepository;
    UserRepository userRepository;
    UserPaymentRepository userPaymentRepository;

    @Override
    public String paymentWithMomo(String amount, String token) {
        try {
            // Prepare request data
            String endpoint = EnvConfig.get("MOMO_ENDPOINT");
            String partnerCode = EnvConfig.get("MOMO_PARTNER_CODE");
            String accessKey = EnvConfig.get("MOMO_ACCESS_KEY");
            String secretKey = EnvConfig.get("MOMO_SECRET_KEY");
            String orderInfo = EnvConfig.get("MOMO_ORDER_INFO");
            String returnUrl = EnvConfig.get("MOMO_RETURN_URL");
            String notifyUrl = EnvConfig.get("MOMO_NOTIFY_URL");

            String orderId = generateUniqueId();
            String requestId = generateUniqueId();
            String extraData = "";

            // Generate the raw hash and sign it
            String rawHash = buildRawHash(partnerCode, accessKey, requestId, amount, orderId, orderInfo, returnUrl, notifyUrl, extraData);
            String signature = moMoSecurity.signSHA256(rawHash, secretKey);

            // Build JSON request body
            JSONObject requestBody = buildRequestBody(partnerCode, accessKey, requestId, amount, orderId, orderInfo, returnUrl, notifyUrl, extraData, signature);

            // Send payment request to MoMo
            String responseFromMomo = sendPaymentRequest(endpoint, requestBody.toString());

            // Handle and parse the response
            return processMomoResponse(responseFromMomo, amount, token, orderId);

        } catch (Exception e) {
            log.error("Error initiating MoMo payment: {}", e.getMessage(), e);
            throw new RuntimeException("Error initiating MoMo payment: " + e.getMessage(), e);
        }
    }

    private String buildRawHash(String partnerCode, String accessKey, String requestId, String amount, String orderId,
                                String orderInfo, String returnUrl, String notifyUrl, String extraData) {
        return "partnerCode=" + partnerCode +
                "&accessKey=" + accessKey +
                "&requestId=" + requestId +
                "&amount=" + amount +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&returnUrl=" + returnUrl +
                "&notifyUrl=" + notifyUrl +
                "&extraData=" + extraData;
    }

    private JSONObject buildRequestBody(String partnerCode, String accessKey, String requestId, String amount,
                                        String orderId, String orderInfo, String returnUrl, String notifyUrl,
                                        String extraData, String signature) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("partnerCode", partnerCode);
        requestBody.put("accessKey", accessKey);
        requestBody.put("requestId", requestId);
        requestBody.put("amount", amount);
        requestBody.put("orderId", orderId);
        requestBody.put("orderInfo", orderInfo);
        requestBody.put("returnUrl", returnUrl);
        requestBody.put("notifyUrl", notifyUrl);
        requestBody.put("extraData", extraData);
        requestBody.put("requestType", "captureMoMoWallet");
        requestBody.put("signature", signature);
        return requestBody;
    }

    private String processMomoResponse(String responseFromMomo, String amount, String token, String orderId) {
        JSONObject response = new JSONObject(responseFromMomo);

        if (response.has("payUrl")) {
            String payUrl = response.getString("payUrl");

            saveOrderToDatabase(amount, token, orderId);

            return payUrl;
        } else {
            throw new RuntimeException("Failed to get payment URL from MoMo response.");
        }
    }

    private void saveOrderToDatabase(String amount, String token, String orderId) {
        OrderEntity order = new OrderEntity();
        order.setTransactionToken(""); // Adjust as needed
        order.setOrderIdMomo(orderId);
        order.setMethod("MOMO");
        order.setAmount(Double.valueOf(amount));
        order.setStatus("SUCCESS");
        order.setToken(token);

        var user = userRepository.getMyInfo();
        order.setUserId(user.getId());
        orderRepository.save(order);

        updateUserBalance(user.getId(), amount);
    }

    private void updateUserBalance(int userId, String amount) {
        var userPayment = userPaymentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User payment record not found"));

        userPayment.setBalance(userPayment.getBalance() + Double.valueOf(amount));
        userPaymentRepository.save(userPayment);
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

    private String generateUniqueId() {
        return String.valueOf(Instant.now().toEpochMilli());
    }
}
