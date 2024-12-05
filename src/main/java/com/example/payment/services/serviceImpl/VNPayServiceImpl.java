package com.example.payment.services.serviceImpl;
import com.example.payment.configuration.VNPAYConfig;
import com.example.payment.repositories.OrderRepository;
import com.example.payment.repositories.UserRepository;
import com.example.payment.repositories.entity.OrderEntity;
import com.example.payment.services.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VNPayServiceImpl implements VNPayService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private static final String VNP_VERSION = "2.1.0";
    private static final String VNP_COMMAND = "pay";
    private static final String VNP_CURRENCY_CODE = "VND";
    private static final String ORDER_TYPE = "order-type";
    private static final String LOCALE = "vn";
    private static final int EXPIRATION_MINUTES = 15;

    @Override
    public String createOrder(HttpServletRequest request, String token, int amount, String orderInfo, String returnUrl) {
        // Generate order and save it to the database
        String transactionRef = VNPAYConfig.getRandomNumber(8);
        OrderEntity order = createAndSaveOrder(token, amount, transactionRef);

        // Build VNPAY parameters
        Map<String, String> vnpParams = buildVnpParams(request, transactionRef, amount, orderInfo, returnUrl);

        // Generate the secure hash and payment URL
        String paymentUrl = buildPaymentUrl(vnpParams);

        return paymentUrl;
    }

    @Override
    public int orderReturn(HttpServletRequest request) {
        Map<String, String> fields = extractRequestParams(request);

        // Validate the secure hash
        String vnpSecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");
        String calculatedHash = VNPAYConfig.hashAllFields(fields);

        if (!calculatedHash.equals(vnpSecureHash)) {
            return -1; // Invalid signature
        }

        // Check transaction status
        String transactionStatus = request.getParameter("vnp_TransactionStatus");
        return "00".equals(transactionStatus) ? 1 : 0; // 1 = Success, 0 = Failure
    }

    private OrderEntity createAndSaveOrder(String token, int amount, String transactionRef) {
        OrderEntity order = new OrderEntity();
        order.setTransactionToken(transactionRef);
        order.setMethod("VNPAY");
        order.setAmount((double) amount);
        order.setStatus("PENDING");
        order.setToken(token);
        orderRepository.save(order);

        // Retrieve and associate the user
        var user = userRepository.getMyInfo();
        order.setUserId(user.getId());
        orderRepository.save(order);

        return order;
    }

    private Map<String, String> buildVnpParams(HttpServletRequest request, String transactionRef, int amount, String orderInfo, String returnUrl) {
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", VNP_VERSION);
        vnpParams.put("vnp_Command", VNP_COMMAND);
        vnpParams.put("vnp_TmnCode", VNPAYConfig.VNP_TMN_CODE);
        vnpParams.put("vnp_Amount", String.valueOf(amount * 100));
        vnpParams.put("vnp_CurrCode", VNP_CURRENCY_CODE);
        vnpParams.put("vnp_TxnRef", transactionRef);
        vnpParams.put("vnp_OrderInfo", orderInfo);
        vnpParams.put("vnp_OrderType", ORDER_TYPE);
        vnpParams.put("vnp_Locale", LOCALE);

        // Add return URL and IP address
        vnpParams.put("vnp_ReturnUrl", returnUrl + VNPAYConfig.VNP_RETURN_URL);
        vnpParams.put("vnp_IpAddr", VNPAYConfig.getIpAddress(request));

        // Add timestamps
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        String createDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_CreateDate", createDate);

        cld.add(Calendar.MINUTE, EXPIRATION_MINUTES);
        String expireDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_ExpireDate", expireDate);

        return vnpParams;
    }

    private String buildPaymentUrl(Map<String, String> vnpParams) {
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = vnpParams.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                try {
                    hashData.append(fieldName).append("=").append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())).append("&");
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append("=")
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())).append("&");
                } catch (UnsupportedEncodingException e) {
                    log.error("Error encoding field: {}", fieldName, e);
                }
            }
        }

        // Remove the trailing "&" and generate the secure hash
        hashData.setLength(hashData.length() - 1); // Remove last "&"
        query.setLength(query.length() - 1); // Remove last "&"
        String secureHash = VNPAYConfig.hmacSHA512(VNPAYConfig.VNP_HASH_SECRET, hashData.toString());

        // Append the secure hash to the query string
        return VNPAYConfig.VNP_PAY_URL + "?" + query + "&vnp_SecureHash=" + secureHash;
    }

    private Map<String, String> extractRequestParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        for (Enumeration<String> names = request.getParameterNames(); names.hasMoreElements(); ) {
            String name = names.nextElement();
            String value = request.getParameter(name);
            if (value != null && !value.isEmpty()) {
                params.put(name, value);
            }
        }
        return params;
    }
}
