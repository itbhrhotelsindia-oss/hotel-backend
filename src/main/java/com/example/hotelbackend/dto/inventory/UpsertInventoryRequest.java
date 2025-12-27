package com.example.hotelbackend.dto.inventory;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpsertInventoryRequest {

    private String hotelId;
    private String roomTypeId;

    private LocalDate startDate;
    private LocalDate endDate;

    private int totalRooms;
    private double pricePerNight;
}

