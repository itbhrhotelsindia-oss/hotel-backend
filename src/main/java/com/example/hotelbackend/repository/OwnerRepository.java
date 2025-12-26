package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.Owner;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OwnerRepository extends MongoRepository<Owner, String> {

    Optional<Owner> findByUsername(String username);
}

