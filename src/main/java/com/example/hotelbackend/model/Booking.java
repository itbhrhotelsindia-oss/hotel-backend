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

    // ✅ PRICING (selected by user)
    private String pricingType;     // ROOM_ONLY, ROOM_WITH_BREAKFAST, ROOM_WITH_MEALS
    private String payMode;         // PAY_NOW, PAY_AT_HOTEL
    private double pricePerNight;
    private double totalAmount;

    private String status;          // PENDING, CONFIRMED, CANCELLED
    // PENDING, CONFIRMED, CANCELLED

    // ✅ Razorpay fields
    private String razorpayOrderId;
    private String razorpayPaymentId;

    // Guest info
    private String guestName;
    private String guestEmail;
    private String guestPhone;

    private LocalDateTime createdAt;
    // Payment expiry handling
    private LocalDateTime paymentExpiresAt;   // when booking should auto-cancel
    private LocalDateTime cancelledAt;         // optional audit
    private String cancelReason;               // PAYMENT_TIMEOUT / PAYMENT_FAILED

}

