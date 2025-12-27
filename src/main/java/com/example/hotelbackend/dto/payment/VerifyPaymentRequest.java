package com.example.hotelbackend.dto.payment;

import lombok.Data;

@Data
public class VerifyPaymentRequest {

    private String bookingId;
    private String state;
}

