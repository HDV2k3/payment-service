package com.example.payment.repositories;

import com.example.payment.repositories.entity.UserPaymentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPaymentRepository extends MongoRepository<UserPaymentEntity,String> {
    Optional<UserPaymentEntity> findByUserId(int userId);
     boolean existsByUserId(int userId);
}
