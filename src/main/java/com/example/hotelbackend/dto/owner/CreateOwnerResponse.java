package com.example.hotelbackend.dto.owner;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateOwnerResponse {

    private String ownerId;
    private String username;
    private List<String> hotelIds;
    private String message;
}

