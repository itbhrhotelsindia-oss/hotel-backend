package com.example.hotelbackend.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "partner_with_us_enquiries")
public class PartnerWithUsEnquiry {

    @Id
    private String id;

    // ❌ Optional
    private String companyName;

    // ❌ Optional
    private String website;

    // ✅ REQUIRED
    @NotBlank(message = "Full name is required")
    private String fullName;

    // ❌ NOT REQUIRED
    // ✅ If provided, must be valid email
    @Email(message = "Invalid email format")
    private String email;

    // ✅ REQUIRED
    @NotBlank(message = "Contact number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Contact number must be exactly 10 digits"
    )
    private String contactNumber;

    // ❌ Optional
    private String partnershipType;

    // ❌ Optional
    private String location;

    // ❌ Optional
    private String message;

    // Metadata
    private LocalDateTime createdAt;
}

