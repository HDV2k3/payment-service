package com.example.payment.repositories;

import com.example.payment.repositories.entity.ListingsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingsRepository extends MongoRepository<ListingsEntity,String> {
}
