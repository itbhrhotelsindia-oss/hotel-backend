package com.example.hotelbackend.dto.inventory;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateInventoryStatusRequest {

    private String hotelId;
    private String roomTypeId;

    private LocalDate date;

    private boolean active;   // false = blocked, true = open
}

