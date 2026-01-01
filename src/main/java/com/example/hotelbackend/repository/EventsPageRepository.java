package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.EventsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EventsPageRepository
        extends MongoRepository<EventsPage, String> {

    Optional<EventsPage> findFirstBy();
}

