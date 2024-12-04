//package com.example.payment.services.serviceImpl;
//import com.example.payment.controller.dto.reponse.UserPaymentResponse;
//import com.example.payment.controller.dto.request.UserPaymentRequest;
//import com.example.payment.exception.AppException;
//import com.example.payment.exception.ErrorCode;
//import com.example.payment.mappers.UserPaymentMapper;
//import com.example.payment.repositories.UserPaymentRepository;
//import com.example.payment.repositories.UserRepository;
//import com.example.payment.repositories.entity.UserPaymentEntity;
//import com.example.payment.services.UserPaymentService;
//import jakarta.transaction.Transactional;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//
//@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@Slf4j
//public class UserPaymentServiceImpl implements UserPaymentService {
//    UserPaymentRepository userPaymentRepository;
//    UserRepository userRepository;
//    UserPaymentMapper userPaymentMapper;
//    final static double BALANCE = 0;
//
//    @Override
//    @Transactional
//    public UserPaymentResponse created( ) {
//        var user = userRepository.getMyInfo();
//        if (userPaymentRepository.existsByUserId(user.getId())) {
//            throw new AppException(ErrorCode.USER_SERVICE_UNAVAILABLE);
//        }
//
//        var userPayment = userPaymentRepository.save(UserPaymentEntity.builder()
//                .userId(user.getId())
//                .balance(BALANCE)
//                .build());
//        return UserPaymentResponse.builder()
//                .id(userPayment.getId())
//                .balance(userPayment.getBalance())
//                .userResponse(user)
//                .createdBy(userPayment.getCreatedBy())
//                .lastModifiedDate(userPayment.getLastModifiedDate())
//                .createDate(userPayment.getCreatedDate())
//                .modifiedBy(userPayment.getModifiedBy())
//                .build();
//    }
//
//    @Override
//    public UserPaymentResponse getUserPayment() {
//        var user = userRepository.getMyInfo();
//        var userPayment = userPaymentRepository.findByUserId(user.getId()).orElseThrow(
//                () -> new AppException(ErrorCode.USER_NOT_FOUND)
//        );
//        return UserPaymentResponse.builder()
//                .id(userPayment.getId())
//                .balance(userPayment.getBalance())
//                .userResponse(user)
//                .createdBy(userPayment.getCreatedBy())
//                .lastModifiedDate(userPayment.getLastModifiedDate())
//                .createDate(userPayment.getCreatedDate())
//                .modifiedBy(userPayment.getModifiedBy())
//                .build();
//    }
//
//    @Override
//    public UserPaymentResponse updateUserPayment(UserPaymentRequest request) {
//        var user = userRepository.getMyInfo();
//        var userPayment = userPaymentRepository.findByUserId(user.getId());
//        return userPaymentRepository.findById(userPayment.get().getId()).map(
//                entity -> {
//                    userPaymentMapper.updateUserPayment(request, entity);
//                    var userPaymentUpdate = userPaymentRepository.save(entity);
//                    return UserPaymentResponse.builder()
//                            .id(userPaymentUpdate.getId())
//                            .balance(userPaymentUpdate.getBalance())
//                            .userResponse(user)
//                            .createdBy(userPaymentUpdate.getCreatedBy())
//                            .lastModifiedDate(userPaymentUpdate.getLastModifiedDate())
//                            .createDate(userPaymentUpdate.getCreatedDate())
//                            .modifiedBy(userPaymentUpdate.getModifiedBy())
//                            .build();
//                }
//
//        ).orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
//    }
//    public UserPaymentEntity updateBalance(UserPaymentEntity userPaymentEntity, Double amount) {
//        // Cộng số tiền vào balance hiện tại
//        Double newBalance = userPaymentEntity.getBalance() + amount;
//
//        // Cập nhật số dư mới
//        userPaymentEntity.setBalance(newBalance);
//
//        // Cập nhật thời gian chỉnh sửa
//        userPaymentEntity.setLastModifiedDate(Instant.now());
//
//        // Trả về entity đã cập nhật
//        return userPaymentEntity;
//    }
//    public UserPaymentEntity updateUserBalance(UserPaymentEntity userPaymentEntity, Double amount) {
//        // Cập nhật balance trong entity
//        userPaymentEntity = updateBalance(userPaymentEntity, amount);
//
//        // Lưu lại entity vào cơ sở dữ liệu
//        return userPaymentRepository.save(userPaymentEntity); // Giả sử bạn có `userPaymentRepository`
//    }
//
//}
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

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserPaymentServiceImpl implements UserPaymentService {

    UserPaymentRepository userPaymentRepository;
    UserRepository userRepository;
    UserPaymentMapper userPaymentMapper;
    private static final double BALANCE = 0;

    @Override
    @Transactional
    public UserPaymentResponse created() {
        var user = userRepository.getMyInfo();
        if (userPaymentRepository.existsByUserId(user.getId())) {
            throw new AppException(ErrorCode.USER_SERVICE_UNAVAILABLE);
        }

        var userPayment = userPaymentRepository.save(UserPaymentEntity.builder()
                .userId(user.getId())
                .balance(BALANCE)
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

    @Override
    public UserPaymentResponse updateUserPayment(UserPaymentRequest request) {
        var user = userRepository.getMyInfo();
        var userPayment = userPaymentRepository.findByUserId(user.getId());

        return userPaymentRepository.findById(userPayment.get().getId()).map(
                entity -> {
                    userPaymentMapper.updateUserPayment(request, entity);
                    var userPaymentUpdate = userPaymentRepository.save(entity);
                    return UserPaymentResponse.builder()
                            .id(userPaymentUpdate.getId())
                            .balance(userPaymentUpdate.getBalance())
                            .userResponse(user)
                            .createdBy(userPaymentUpdate.getCreatedBy())
                            .lastModifiedDate(userPaymentUpdate.getLastModifiedDate())
                            .createDate(userPaymentUpdate.getCreatedDate())
                            .modifiedBy(userPaymentUpdate.getModifiedBy())
                            .build();
                }
        ).orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
    }

    public UserPaymentEntity updateBalance(UserPaymentEntity userPaymentEntity, Double amount) {
        // Cộng số tiền vào balance hiện tại
        Double newBalance = userPaymentEntity.getBalance() + amount;

        // Cập nhật số dư mới
        userPaymentEntity.setBalance(newBalance);

        // Cập nhật thời gian chỉnh sửa
        userPaymentEntity.setLastModifiedDate(Instant.now());

        // Trả về entity đã cập nhật
        return userPaymentEntity;
    }

    @Override
    public UserPaymentEntity updateUserBalance(Double amount) {
        var user = userRepository.getMyInfo();
        var userPayment = userPaymentRepository.findByUserId(user.getId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        // Cập nhật balance trong entity
        userPayment = updateBalance(userPayment, amount);

        // Lưu lại entity vào cơ sở dữ liệu
        return userPaymentRepository.save(userPayment);
    }
}
