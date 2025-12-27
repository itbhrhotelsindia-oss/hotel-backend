package com.example.hotelbackend.controller;

import com.example.hotelbackend.dto.payment.CreatePaymentRequest;
import com.example.hotelbackend.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/payments/phonepe")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /* =========================================================
       STEP 9.1 — CREATE PHONEPE PAYMENT ORDER
       ========================================================= */

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(
            @RequestBody CreatePaymentRequest request
    ) {
        return ResponseEntity.ok(
                paymentService.createOrder(request.getBookingId())
        );
    }

    /* =========================================================
       OPTIONAL — REDIRECT HANDLER (NO FRONTEND YET)
       ========================================================= */

    @GetMapping("/redirect")
    public ResponseEntity<String> redirectPage() {
        return ResponseEntity.ok(
                "Payment is being processed. You may close this page."
        );
    }
}
