package com.example.payment.repositories;

import com.example.payment.repositories.entity.TransactionsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepository extends MongoRepository<TransactionsEntity,String> {
}
