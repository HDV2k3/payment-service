package com.example.payment.services.serviceImpl;

import com.example.payment.exception.AppException;
import com.example.payment.exception.ErrorCode;
import com.example.payment.repositories.OrderRepository;
import com.example.payment.repositories.UserPaymentRepository;
import com.example.payment.repositories.UserRepository;
import com.example.payment.repositories.entity.OrderEntity;
import com.example.payment.repositories.entity.UserPaymentEntity;
import com.example.payment.services.MobilePaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class MobilePaymentServiceImpl implements MobilePaymentService {

    UserRepository userRepository;
    OrderRepository orderRepository;
    UserPaymentRepository userPaymentRepository;

    @Override
    public String paymentWithMobile(String amount, String method) {
        // Retrieve the user information
        var user = userRepository.getMyInfo();

        // Create a new order entity
        var order = OrderEntity.builder()
                .amount(Double.valueOf(amount)) // Convert amount from String to Double
                .orderIdMomo("") // Assuming orderIdMomo is not used here
                .transactionToken("") // Assuming transactionToken is not used here
                .method(method) // Payment method (mobile)
                .status("SUCCESS") // Set the status as "SUCCESS"
                .userId(user.getId()) // Link to the current user's ID
                .build();

        try {
            // Save the new order
            var saved = orderRepository.save(order);

            // Check if the order was saved correctly and has a valid method and amount
            if (saved.getMethod() != null && saved.getAmount() != null) {
                // Find the user payment record for this user
                var userPayment = userPaymentRepository.findByUserId(saved.getUserId())
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

                // Update the balance (keep other fields unchanged)
                double newBalance = userPayment.getBalance() + saved.getAmount();

                // Set the updated balance in the userPayment entity
                userPayment.setBalance(newBalance);

                // Save the updated user payment entity
                userPaymentRepository.save(userPayment);
            }
        } catch (Exception e) {
            // Log the exception and rethrow as an application exception
            log.error("Error processing payment: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        // Return a success message
        return "SUCCESS";
    }
}
