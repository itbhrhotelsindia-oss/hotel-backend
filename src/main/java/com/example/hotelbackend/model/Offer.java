package com.example.hotelbackend.model;

import lombok.Data;

@Data
public class Offer {
    private int id;
    private String title;
    private String desc;
    private String validity;
    private String img;
    private String loginBtn;
}

