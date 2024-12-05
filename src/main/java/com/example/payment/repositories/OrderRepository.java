package com.example.payment.repositories;

import com.example.payment.repositories.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<OrderEntity, String> {
    Optional<OrderEntity> findByTransactionToken(String transactionToken);
    Page<OrderEntity> findAllByUserId(int userId, Pageable pageable);
}