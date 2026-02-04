package com.example.hotelbackend.controller;

import com.example.hotelbackend.dto.rozarpay.RazorpayOrderRequest;
import com.example.hotelbackend.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/payments/razorpay")
public class RazorpayPaymentController {

    private final PaymentService paymentService;

    public RazorpayPaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(
            @RequestBody RazorpayOrderRequest request
    ) {
        return ResponseEntity.ok(
                paymentService.createOrder(request.getBookingId())
        );
    }
}
