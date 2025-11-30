package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByUserId(String userId);
    List<Booking> findByHotelId(String hotelId);
}
