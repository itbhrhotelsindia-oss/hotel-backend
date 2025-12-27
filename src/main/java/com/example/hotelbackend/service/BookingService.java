package com.example.hotelbackend.service;

import com.example.hotelbackend.dto.booking.CreateBookingRequest;
import com.example.hotelbackend.model.Booking;

public interface BookingService {

    Booking createPendingBooking(CreateBookingRequest request);
}

