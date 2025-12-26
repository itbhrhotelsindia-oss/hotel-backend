package com.example.hotelbackend.dto.booking;

import lombok.Data;

@Data
public class CheckAvailabilityRequest {

    private String hotelId;
    private String roomTypeId;

    // yyyy-MM-dd
    private String checkIn;
    private String checkOut;

    private int roomsRequested;
}

