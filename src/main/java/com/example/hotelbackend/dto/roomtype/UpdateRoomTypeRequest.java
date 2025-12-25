package com.example.hotelbackend.dto.roomtype;

import lombok.Data;

@Data
public class UpdateRoomTypeRequest {

    private String name;
    private String description;
    private int maxGuests;
}
