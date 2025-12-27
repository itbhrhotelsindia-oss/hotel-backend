package com.example.hotelbackend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "bookings")
public class Booking {

    @Id
    private String id;

    private String bookingId;   // BHR-20260110-0001

    private String hotelId;
    private String roomTypeId;

    private LocalDate checkIn;
    private LocalDate checkOut;

    private int rooms;
    private int nights;

    private double totalAmount;

    private String status;      // PENDING, CONFIRMED, CANCELLED

    // Guest info
    private String guestName;
    private String guestEmail;
    private String guestPhone;

    private LocalDateTime createdAt;
}

