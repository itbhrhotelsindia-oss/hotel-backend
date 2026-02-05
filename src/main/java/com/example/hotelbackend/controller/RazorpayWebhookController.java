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

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature
    ) {
        try {
            // ✅ Verify webhook signature
            Utils.verifyWebhookSignature(payload, signature, webhookSecret);

            JSONObject json = new JSONObject(payload);

            JSONObject payment =
                    json.getJSONObject("payload")
                            .getJSONObject("payment")
                            .getJSONObject("entity");

            // ✅ Always available
            String razorpayOrderId = payment.getString("order_id");
            String status = payment.getString("status");

            VerifyPaymentRequest request = new VerifyPaymentRequest();
            request.setRazorpayOrderId(razorpayOrderId);
            request.setState(
                    "captured".equalsIgnoreCase(status)
                            ? "SUCCESS"
                            : "FAILED"
            );

            paymentService.verifyAndConfirmPayment(request);

            System.out.println("✅ Webhook processed for orderId: " + razorpayOrderId);
            return ResponseEntity.ok("Webhook processed");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid webhook");
        }
    }
}
