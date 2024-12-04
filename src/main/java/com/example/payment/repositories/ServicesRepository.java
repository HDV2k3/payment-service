package com.example.payment.repositories;

import com.example.payment.repositories.entity.ServicesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesRepository extends MongoRepository<ServicesEntity,String> {
}
