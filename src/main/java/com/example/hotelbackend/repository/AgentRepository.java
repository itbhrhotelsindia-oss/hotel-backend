package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.Agent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AgentRepository
        extends MongoRepository<Agent, String> {

    Optional<Agent> findByUsername(String username);

}