package com.example.hotelbackend.controller;

import com.example.hotelbackend.dto.payment.VerifyPaymentRequest;
import com.example.hotelbackend.service.PaymentService;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/payments/razorpay")
public class RazorpayWebhookController {

    private final PaymentService paymentService;

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    public RazorpayWebhookController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /* =========================
       RAZORPAY WEBHOOK
       ========================= */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature
    ) {
        try {
            // ✅ 1. Verify webhook signature (MANDATORY)
            Utils.verifyWebhookSignature(payload, signature, webhookSecret);

            System.out.println("🔥 Razorpay webhook verified");
            System.out.println(payload);

            // ✅ 2. Parse payload
            JSONObject json = new JSONObject(payload);

            JSONObject paymentEntity =
                    json.getJSONObject("payload")
                            .getJSONObject("payment")
                            .getJSONObject("entity");

            // ✅ 3. Read bookingId from notes
            String bookingId =
                    paymentEntity
                            .getJSONObject("notes")
                            .getString("bookingId");

            // Razorpay payment status
            String status = paymentEntity.getString("status");

            // ✅ 4. Map Razorpay status → internal status
            VerifyPaymentRequest request = new VerifyPaymentRequest();
            request.setBookingId(bookingId);
            request.setState(
                    "captured".equalsIgnoreCase(status)
                            ? "SUCCESS"
                            : "FAILED"
            );

            // ✅ 5. Update booking in DB
            paymentService.verifyAndConfirmPayment(request);

            System.out.println("✅ Booking updated via webhook: " + bookingId);

            return ResponseEntity.ok("Webhook processed successfully");

        } catch (Exception e) {
            System.err.println("❌ Razorpay webhook failed");
            e.printStackTrace();
            return ResponseEntity.status(400).body("Invalid webhook");
        }
    }
}
