package com.example.hotelbackend.controller;

import com.example.hotelbackend.dto.payment.VerifyPaymentRequest;
import com.example.hotelbackend.service.PaymentService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments/razorpay")
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
        // ⚠️ Signature verification will be added later
        JSONObject json = new JSONObject(payload);

        JSONObject paymentEntity =
                json.getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity");

        String bookingId =
                paymentEntity.getJSONObject("notes")
                        .getString("bookingId");

        String status = paymentEntity.getString("status");

        VerifyPaymentRequest request = new VerifyPaymentRequest();
        request.setBookingId(bookingId);
        request.setState(
                "captured".equals(status) ? "SUCCESS" : "FAILED"
        );

        paymentService.verifyAndConfirmPayment(request);

        return ResponseEntity.ok("Webhook processed");
    }
}
