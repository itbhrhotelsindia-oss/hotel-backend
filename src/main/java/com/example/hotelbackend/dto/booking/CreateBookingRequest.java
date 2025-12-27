package com.example.hotelbackend.dto.booking;

import lombok.Data;

@Data
public class CreateBookingRequest {

    private String hotelId;
    private String roomTypeId;

    private String checkIn;
    private String checkOut;

    private int rooms;

    // Guest details
    private String guestName;
    private String guestEmail;
    private String guestPhone;
}

