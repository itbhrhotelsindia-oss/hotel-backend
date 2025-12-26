package com.example.hotelbackend.service;

import com.example.hotelbackend.dto.booking.CheckAvailabilityRequest;
import com.example.hotelbackend.dto.booking.AvailabilityResponse;

public interface BookingAvailabilityService {
    Object checkAvailability(CheckAvailabilityRequest request);
}

