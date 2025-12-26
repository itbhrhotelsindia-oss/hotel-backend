package com.example.hotelbackend.dto.inventory;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateInventoryForDateRequest {

    private String hotelId;
    private String roomTypeId;

    private LocalDate date;

    private Integer totalRooms;      // optional
    private Double pricePerNight;    // optional
}

