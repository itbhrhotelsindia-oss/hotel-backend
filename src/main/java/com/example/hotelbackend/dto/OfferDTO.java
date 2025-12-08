package com.example.hotelbackend.dto;

import lombok.Data;

@Data
public class OfferDTO {
    private int id;
    private String title;
    private String desc;
    private String validity;
    private String img;
    private String loginBtn;
}

