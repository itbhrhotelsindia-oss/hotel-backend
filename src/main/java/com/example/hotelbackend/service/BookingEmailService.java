package com.example.hotelbackend.service;

import com.example.hotelbackend.model.Booking;

public interface BookingEmailService {

    void bookingCreated(Booking booking);

    void bookingConfirmed(Booking booking);

    void bookingCancelled(Booking booking);
}

