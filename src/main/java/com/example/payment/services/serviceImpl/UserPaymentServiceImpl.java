package com.example.payment.services.serviceImpl;

import com.example.payment.controller.dto.reponse.UserPaymentResponse;
import com.example.payment.controller.dto.request.BaseIndexRequest;
import com.example.payment.controller.dto.request.UserPaymentRequest;
import com.example.payment.exception.AppException;
import com.example.payment.exception.ErrorCode;
import com.example.payment.mappers.UserPaymentMapper;
import com.example.payment.repositories.OrderRepository;
import com.example.payment.repositories.RoomRepository;
import com.example.payment.repositories.UserPaymentRepository;
import com.example.payment.repositories.UserRepository;
import com.example.payment.repositories.entity.OrderEntity;
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
    OrderRepository orderRepository;
    RoomRepository roomRepository;
    @Override
    @Transactional
    public UserPaymentResponse created(int userId) {

        if (userPaymentRepository.existsByUserId(userId)) {
            throw new AppException(ErrorCode.USER_SERVICE_UNAVAILABLE);
        }

        var userPayment = userPaymentRepository.save(UserPaymentEntity.builder()
                .userId(userId)
                .balance(BALANCE)
                .build());
        return UserPaymentResponse.builder()
                .id(userPayment.getId())
                .balance(userPayment.getBalance())
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

    @Override
    public void updateUserBalance(String transactionToken) {
        var user = userRepository.getMyInfo();
        var userPayment = userPaymentRepository.findByUserId(user.getId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        var orderVNPay = orderRepository.findByTransactionToken(transactionToken).orElseThrow(
                () -> new AppException(ErrorCode.ROOM_NOT_FOUND)
        );
        if (orderVNPay != null) {
            Double amount = orderVNPay.getAmount();
            userPayment = updateBalance(userPayment, amount);
            userPaymentRepository.save(userPayment);
            orderVNPay.setStatus("SUCCESS");
            orderRepository.save(orderVNPay);
        }
    }

    @Override
    public void minusBalance(int type,String roomId) {
        var user = userRepository.getMyInfo();
        var userPayment = userPaymentRepository.findByUserId(user.getId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        userPayment =updateBalanceWithPackage(userPayment,type, userPayment.getUserId());
        userPaymentRepository.save(userPayment);
//        roomRepository.featuredRoomAdsFee(type, roomId);
    }
    public UserPaymentEntity updateBalance(UserPaymentEntity userPaymentEntity, Double amount) {
        Double newBalance = userPaymentEntity.getBalance() + amount;
        userPaymentEntity.setBalance(newBalance);
        userPaymentEntity.setLastModifiedDate(Instant.now());
        return userPaymentEntity;
    }
    public UserPaymentEntity updateBalanceWithPackage(UserPaymentEntity userPaymentEntity, int typePackage,int userId) {
        Double amount=  0.0;
        if (typePackage==1)
        {
            amount= 10000.0;
        }
        if (typePackage==2)
        {
            amount=50000.0;
        }
        if (typePackage==3)
        {
            amount=189000.0;
        }
        Double newBalance = userPaymentEntity.getBalance() - amount;
        userPaymentEntity.setBalance(newBalance);
        userPaymentEntity.setLastModifiedDate(Instant.now());
        orderRepository.save(OrderEntity.builder()
                .orderIdMomo("")
                .transactionToken("")
                .amount(amount)
                .userId(userId)
                .status("SUCCESS")
                .method("ADS_FEE")
                .build()
        );
        return userPaymentEntity;
    }
}
