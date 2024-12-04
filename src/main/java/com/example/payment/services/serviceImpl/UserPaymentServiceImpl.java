package com.example.payment.services.serviceImpl;

import com.example.payment.controller.dto.reponse.UserPaymentResponse;
import com.example.payment.controller.dto.request.UserPaymentRequest;
import com.example.payment.exception.AppException;
import com.example.payment.exception.ErrorCode;
import com.example.payment.mappers.UserPaymentMapper;
import com.example.payment.repositories.UserPaymentRepository;
import com.example.payment.repositories.UserRepository;
import com.example.payment.repositories.entity.UserPaymentEntity;
import com.example.payment.services.UserPaymentService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserPaymentServiceImpl implements UserPaymentService {
    UserPaymentRepository userPaymentRepository;
    UserRepository userRepository;
    UserPaymentMapper userPaymentMapper;

    @Override
    @Transactional
    public UserPaymentResponse created(UserPaymentRequest request) {
        var user = userRepository.getMyInfo();
        if (userPaymentRepository.existsByUserId(user.getId())) {
           throw new AppException(ErrorCode.USER_SERVICE_UNAVAILABLE);
        }
        if (request.getBalance() == null || request.getBalance() <= 0) {
            throw new AppException(ErrorCode.USER_SERVICE_UNAVAILABLE);
        }
        var userPayment = userPaymentRepository.save(UserPaymentEntity.builder()
                .userId(user.getId())
                .balance(request.getBalance())
                .build());
        return UserPaymentResponse.builder()
                .id(userPayment.getId())
                .balance(userPayment.getBalance())
                .userResponse(user)
                .createdBy(userPayment.getCreatedBy())
                .lastModifiedDate(userPayment.getLastModifiedDate())
                .createDate(userPayment.getCreatedDate())
                .modifiedBy(userPayment.getModifiedBy())
                .build();
    }

    @Override
    public UserPaymentResponse getUserPayment() {
        var user = userRepository.getMyInfo();
        var userPayment = userPaymentRepository.findByUserId(user.getId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        return UserPaymentResponse.builder()
                .id(userPayment.getId())
                .balance(userPayment.getBalance())
                .userResponse(user)
                .createdBy(userPayment.getCreatedBy())
                .lastModifiedDate(userPayment.getLastModifiedDate())
                .createDate(userPayment.getCreatedDate())
                .modifiedBy(userPayment.getModifiedBy())
                .build();
    }
}
