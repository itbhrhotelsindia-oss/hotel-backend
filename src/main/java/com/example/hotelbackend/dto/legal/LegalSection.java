package com.example.hotelbackend.dto.legal;

import lombok.Getter;

@Getter
public class LegalSection {

    private String heading;
    private String body;

    public LegalSection(String heading, String body) {
        this.heading = heading;
        this.body = body;
    }

}

