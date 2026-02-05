package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BookingRepository extends MongoRepository<Booking, String> {
    Optional<Booking> findByBookingId(String bookingId);
    Optional<Booking> findByRazorpayOrderId(String razorpayOrderId);

}


