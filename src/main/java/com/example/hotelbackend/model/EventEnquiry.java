package com.example.hotelbackend.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "event_enquiries")
public class EventEnquiry {

    @Id
    private String id;

    // REQUIRED
    @NotBlank(message = "Name is required")
    private String name;

    // OPTIONAL – validate if present
    @Email(message = "Invalid email format")
    private String email;

    // OPTIONAL (default handled in frontend)
    private String countryCode;

    // REQUIRED
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must be exactly 10 digits"
    )
    private String phone;

    // REQUIRED
    @NotBlank(message = "Event type is required")
    private String eventType;

    // REQUIRED
    @NotBlank(message = "Location is required")
    private String location;

    // OPTIONAL
    private String query;

    // AUTO
    private String enquiryType; // FAMILY_EVENT / BUSINESS_EVENT
    private LocalDateTime createdAt;
}
