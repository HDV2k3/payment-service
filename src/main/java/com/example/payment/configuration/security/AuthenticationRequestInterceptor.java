package com.example.payment.configuration.security;

import com.example.payment.repositories.OrderRepository;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * AuthenticationRequestInterceptor is a Feign RequestInterceptor that retrieves
 * the current request's Authorization header and passes it along in the Feign request.
 * This ensures the token-based authentication is propagated in downstream requests.
 */

@Slf4j
@Component
public class AuthenticationRequestInterceptor implements RequestInterceptor {
    private final OrderRepository orderRepository;  // Inject OrderRepository to retrieve token from OrderEntity

    public AuthenticationRequestInterceptor(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;  // Initialize the repository
    }
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // Lấy các thuộc tính yêu cầu từ RequestContextHolder
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        String authHeader = null;

        if (servletRequestAttributes != null) {
            // Lấy Authorization header từ request hiện tại
            authHeader = servletRequestAttributes.getRequest().getHeader("Authorization");
            log.info("Authorization Header from request: {}", authHeader);
        }

        // Nếu không có Authorization header trong request, thử lấy từ session
        if (!StringUtils.hasText(authHeader)) {
            try {
                if (servletRequestAttributes != null) {
                    // Lấy session hiện tại
                    HttpSession session = servletRequestAttributes.getRequest().getSession(false);
                    if (session != null) {
                        authHeader = (String) session.getAttribute("Authorization");
                        log.info("Authorization Header from session: {}", authHeader);
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to retrieve Authorization header from session: {}", e.getMessage());
            }
        }
        // Nếu không có Authorization token từ request hoặc session, thử lấy từ OrderEntity
        if (!StringUtils.hasText(authHeader)) {
            try {
                String transactionToken = servletRequestAttributes.getRequest().getParameter("vnp_TxnRef");
                if (transactionToken != null) {
                    // Retrieve the token from OrderEntity using transactionToken
                    var orderOptional = orderRepository.findByTransactionToken(transactionToken);
                    if (orderOptional.isPresent()) {
                        authHeader = orderOptional.get().getToken();
                        log.info("Authorization Token retrieved from OrderEntity: {}", authHeader);
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to retrieve Authorization token from OrderEntity: {}", e.getMessage());
            }
        }
        // Nếu không có Authorization token từ request hoặc session, thử lấy từ OrderEntity
        if (!StringUtils.hasText(authHeader)) {
            try {
                String transactionToken = servletRequestAttributes.getRequest().getParameter("orderId");
                if (transactionToken != null) {
                    // Retrieve the token from OrderEntity using transactionToken
                    var orderOptional = orderRepository.findByOrderIdMomo(transactionToken);
                    if (orderOptional.isPresent()) {
                        authHeader = orderOptional.get().getToken();
                        log.info("Authorization Token retrieved from OrderEntity: {}", authHeader);
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to retrieve Authorization token from OrderEntity: {}", e.getMessage());
            }
        }
        // Nếu token tồn tại và bắt đầu với 'Bearer ', cắt bỏ phần tiền tố
        if (StringUtils.hasText(authHeader)) {
            if (authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }
            log.info("Final Authorization Token: {}", authHeader);

            // Thêm token vào header của yêu cầu Feign
            requestTemplate.header("Authorization", "Bearer " + authHeader);
        } else {
            log.warn("Authorization token not found in request or session");
        }
    }
}
