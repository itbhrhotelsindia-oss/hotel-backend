package com.example.hotelbackend.dto.owner;

import lombok.Data;

import java.util.List;

@Data
public class CreateOwnerRequest {

    private String username;
    private String password;        // plain text (only here)
    private List<String> hotelIds;  // linked hotels
}

