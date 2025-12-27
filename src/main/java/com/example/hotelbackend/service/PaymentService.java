package com.example.hotelbackend.service;

import com.example.hotelbackend.dto.payment.VerifyPaymentRequest;
import org.json.JSONObject;

public interface PaymentService {

    JSONObject createOrder(String bookingId);

    void verifyAndConfirmPayment(VerifyPaymentRequest request);
}

