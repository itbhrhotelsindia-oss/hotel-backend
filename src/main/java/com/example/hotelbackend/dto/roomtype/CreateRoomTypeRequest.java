package com.example.hotelbackend.dto.roomtype;

import lombok.Data;

@Data
public class CreateRoomTypeRequest {

    private String hotelId;
    private String name;
    private String description;
    private int maxGuests;
}
