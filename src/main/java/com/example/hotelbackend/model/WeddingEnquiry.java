package com.example.hotelbackend.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;

@Data
@Document(collection = "wedding_enquiries")
public class WeddingEnquiry {

    @Id
    private String id;

    // ✅ REQUIRED
    @NotBlank(message = "Name is required")
    private String name;

    // ❌ NOT REQUIRED
    // ✅ If present, must be valid email
    @Email(message = "Invalid email format")
    private String email;

    // ❌ NOT REQUIRED (frontend provides default +91)
    private String countryCode;

    // ✅ REQUIRED
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must be exactly 10 digits"
    )
    private String phone;

    // ❌ OPTIONAL
    private String city;

    // ❌ OPTIONAL
    private String queryFor;

    // ❌ OPTIONAL
    private String comments;

    private String page;
    private LocalDateTime createdAt;
}
