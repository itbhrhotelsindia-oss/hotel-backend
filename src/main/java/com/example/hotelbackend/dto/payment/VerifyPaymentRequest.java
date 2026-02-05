package com.example.hotelbackend.dto.payment;

import lombok.Data;

@Data
public class VerifyPaymentRequest {

    private String razorpayOrderId;
    private String bookingId;
    private String state;
}

