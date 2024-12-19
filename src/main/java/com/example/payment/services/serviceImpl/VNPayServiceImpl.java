package com.example.payment.services.serviceImpl;
import com.example.payment.configuration.vnpay.VNPAYConfig;
import com.example.payment.configuration.vnpay.VnPayVariable;
import com.example.payment.repositories.OrderRepository;
import com.example.payment.repositories.UserRepository;
import com.example.payment.repositories.entity.OrderEntity;
import com.example.payment.services.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
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
    private final VnPayVariable vnPayVariable;

    public VNPayServiceImpl(UserRepository userRepository, OrderRepository orderRepository, VnPayVariable vnPayVariable) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.vnPayVariable = vnPayVariable;
    }

    @Override
    public String createOrder(HttpServletRequest request, int amount,  String returnUrl) {
        // Generate order and save it to the database
        String transactionRef = VNPAYConfig.getRandomNumber(8);
        createAndSaveOrder( amount, transactionRef);
        // Build VNPAY parameters
        Map<String, String> vnpParams = buildVnpParams(request, transactionRef, amount, returnUrl);
        // Generate the secure hash and payment URL
        return buildPaymentUrl(vnpParams);
    }

    @Override
    public String orderReturn(HttpServletRequest request) {
        Map<String, String> fields = extractRequestParams(request);

        // Validate the secure hash
        String vnpSecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");
        String calculatedHash = VNPAYConfig.hashAllFields(fields);

        if (!calculatedHash.equals(vnpSecureHash)) {
            return vnPayVariable.getError(); // Invalid signature
        }

        // Check transaction status
//        String transactionStatus = request.getParameter("vnp_TransactionStatus");
//        return "00".equals(transactionStatus) ? vnPayVariable.getSuccess() : vnPayVariable.getError(); // 1 = Success, 0 = Failure
        return vnPayVariable.getSuccess();
    }

    private void createAndSaveOrder(int amount, String transactionRef) {
        OrderEntity order = new OrderEntity();
        order.setTransactionToken(transactionRef);
        order.setOrderIdMomo("");
        order.setMethod("VNPAY");
        order.setAmount((double) amount);
        order.setStatus("PENDING");
        var user = userRepository.getMyInfo();
        order.setUserId(user.getId());
        orderRepository.save(order);
    }

    private Map<String, String> buildVnpParams(HttpServletRequest request, String transactionRef, int amount, String returnUrl) {
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", VNP_VERSION);
        vnpParams.put("vnp_Command", VNP_COMMAND);
        vnpParams.put("vnp_TmnCode", vnPayVariable.getTmnCode());
        vnpParams.put("vnp_Amount", String.valueOf(amount * 100));
        vnpParams.put("vnp_CurrCode", VNP_CURRENCY_CODE);
        vnpParams.put("vnp_TxnRef", transactionRef);
        vnpParams.put("vnp_OrderInfo", vnPayVariable.getOrderInfo());
        vnpParams.put("vnp_OrderType", ORDER_TYPE);
        vnpParams.put("vnp_Locale", LOCALE);

        // Add return URL and IP address
        vnpParams.put("vnp_ReturnUrl", returnUrl + vnPayVariable.getReturnUrl());
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
                hashData.append(fieldName).append("=").append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII)).append("&");
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append("=")
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII)).append("&");
            }
        }

        // Remove the trailing "&" and generate the secure hash
        hashData.setLength(hashData.length() - 1); // Remove last "&"
        query.setLength(query.length() - 1); // Remove last "&"
        String secureHash = VNPAYConfig.hmacSHA512(vnPayVariable.getHashSecret(), hashData.toString());

        // Append the secure hash to the query string
        return vnPayVariable.getUrl() + "?" + query + "&vnp_SecureHash=" + secureHash;
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
