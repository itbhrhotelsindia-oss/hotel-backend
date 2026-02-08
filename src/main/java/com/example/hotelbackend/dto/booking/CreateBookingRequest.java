package com.example.hotelbackend.dto.booking;

import lombok.Data;

@Data
public class CreateBookingRequest {

    private String hotelId;
    private String roomTypeId;

    private String checkIn;
    private String checkOut;

    private int rooms;

    // ✅ FROM FRONTEND (selected pricing)
    private String pricingType;     // ROOM_ONLY / ROOM_WITH_BREAKFAST
    private String payMode;         // PAY_NOW / PAY_AT_HOTEL
    private double pricePerNight;
    private double totalAmount;

    // Guest details
    private String guestName;
    private String guestEmail;
    private String guestPhone;
}

