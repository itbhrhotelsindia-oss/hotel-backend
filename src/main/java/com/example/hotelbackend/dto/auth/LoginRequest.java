package com.example.hotelbackend.dto.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}

