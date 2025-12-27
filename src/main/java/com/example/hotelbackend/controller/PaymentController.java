package com.example.hotelbackend.controller;

import com.example.hotelbackend.dto.payment.CreatePaymentRequest;
import com.example.hotelbackend.dto.payment.VerifyPaymentRequest;
import com.example.hotelbackend.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // STEP 9.1 — Create Razorpay order
    @PostMapping("/order")
    public ResponseEntity<?> createOrder(
            @RequestBody CreatePaymentRequest request
    ) {
        return ResponseEntity.ok(
                paymentService.createOrder(request.getBookingId())
        );
    }

    // STEP 9.2 — Verify payment
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(
            @RequestBody VerifyPaymentRequest request
    ) {
        paymentService.verifyAndConfirmPayment(request);
        return ResponseEntity.ok("Payment successful, booking confirmed");
    }
}

