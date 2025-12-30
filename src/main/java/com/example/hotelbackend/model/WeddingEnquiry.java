package com.example.hotelbackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "wedding_enquiries")
public class WeddingEnquiry {

    @Id
    private String id;

    private String name;
    private String email;

    private String countryCode; // +91
    private String phone;

    private String city;
    private String queryFor;    // selected hotel
    private String comments;

    private String page;        // WEDDING
    private LocalDateTime createdAt;
}
