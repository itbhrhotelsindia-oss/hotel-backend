package com.example.hotelbackend.service;

import com.example.hotelbackend.dto.payment.VerifyPaymentRequest;
import org.json.JSONObject;

import java.util.Map;

public interface PaymentService {

    Map<String, Object> createOrder(String bookingId);

    void verifyAndConfirmPayment(VerifyPaymentRequest request);
}

