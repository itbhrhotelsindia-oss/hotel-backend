package com.example.hotelbackend.controller;

import com.example.hotelbackend.dto.payment.VerifyPaymentRequest;
import com.example.hotelbackend.service.PaymentService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@RestController
@RequestMapping("/api/payments/phonepe")
public class PhonePeWebhookController {

    private final PaymentService paymentService;

    @Value("${phonepe.salt.key}")
    private String saltKey;

    @Value("${phonepe.salt.index}")
    private int saltIndex;

    public PhonePeWebhookController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /* =========================================================
       PHONEPE WEBHOOK
       ========================================================= */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String body,
            @RequestHeader("X-VERIFY") String receivedChecksum
    ) {

        try {
            // 1️⃣ Verify checksum
            String calculatedChecksum =
                    sha256(body + saltKey) + "###" + saltIndex;

            if (!calculatedChecksum.equals(receivedChecksum)) {
                return ResponseEntity.status(400).body("Invalid checksum");
            }

            // 2️⃣ Decode payload
            JSONObject json = new JSONObject(body);
            String encodedResponse = json.getString("response");

            String decodedResponse =
                    new String(Base64.getDecoder().decode(encodedResponse), StandardCharsets.UTF_8);

            JSONObject responseJson = new JSONObject(decodedResponse);

            // 3️⃣ Extract values
            String merchantTransactionId =
                    responseJson.getString("merchantTransactionId");

            String state =
                    responseJson.getString("state"); // SUCCESS / FAILED

            // 4️⃣ Call payment service
            VerifyPaymentRequest request = new VerifyPaymentRequest();
            request.setBookingId(merchantTransactionId);
            request.setState(state);

            paymentService.verifyAndConfirmPayment(request);

            return ResponseEntity.ok("Webhook processed");

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Webhook error");
        }
    }

    /* =========================================================
       SHA256 UTILITY
       ========================================================= */
    private String sha256(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}

