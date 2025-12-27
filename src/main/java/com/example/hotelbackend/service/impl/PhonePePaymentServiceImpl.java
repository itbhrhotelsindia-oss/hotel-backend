package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.dto.payment.VerifyPaymentRequest;
import com.example.hotelbackend.model.Booking;
import com.example.hotelbackend.repository.BookingRepository;
import com.example.hotelbackend.service.InventoryRollbackService;
import com.example.hotelbackend.service.PaymentService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class PhonePePaymentServiceImpl implements PaymentService {

    private final BookingRepository bookingRepository;
    private final InventoryRollbackService rollbackService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${phonepe.merchant.id}")
    private String merchantId;

    @Value("${phonepe.salt.key}")
    private String saltKey;

    @Value("${phonepe.salt.index}")
    private int saltIndex;

    @Value("${phonepe.base.url}")
    private String baseUrl;

    public PhonePePaymentServiceImpl(
            BookingRepository bookingRepository,
            InventoryRollbackService rollbackService
    ) {
        this.bookingRepository = bookingRepository;
        this.rollbackService = rollbackService;
    }

    /* =========================================================
       STEP 9.1 — CREATE PHONEPE PAYMENT
       ========================================================= */
    @Override
    public JSONObject createOrder(String bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!"PENDING".equals(booking.getStatus())) {
            throw new RuntimeException("Payment not allowed for booking status");
        }

        try {
            JSONObject payload = new JSONObject();

// 🔑 Merchant details
            payload.put("merchantId", merchantId);
            payload.put("merchantTransactionId", booking.getBookingId());

// 💰 Amount (INR → paise)
            payload.put("amount", (int) (booking.getTotalAmount() * 100));

// 🌐 Redirect (temporary backend redirect, frontend not ready yet)
            payload.put(
                    "redirectUrl",
                    "https://hotel-backend-nq72.onrender.com/api/public/payments/phonepe/redirect"
            );
            payload.put("redirectMode", "GET");

// 🔔 Webhook (MOST IMPORTANT)
            payload.put(
                    "callbackUrl",
                    "https://hotel-backend-nq72.onrender.com/api/payments/phonepe/webhook"
            );

// 📦 Optional metadata (recommended)
            payload.put("paymentInstrument", new JSONObject().put("type", "PAY_PAGE"));

            JSONObject instrument = new JSONObject();
            instrument.put("type", "PAY_PAGE");
            payload.put("paymentInstrument", instrument);

            String base64Payload =
                    Base64.getEncoder().encodeToString(payload.toString().getBytes(StandardCharsets.UTF_8));

            String checksum =
                    sha256(base64Payload + "/pg/v1/pay" + saltKey) + "###" + saltIndex;

            JSONObject requestBody = new JSONObject();
            requestBody.put("request", base64Payload);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-VERIFY", checksum);

            HttpEntity<String> request =
                    new HttpEntity<>(requestBody.toString(), headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(
                            baseUrl + "/pg/v1/pay",
                            request,
                            String.class
                    );

            return new JSONObject(response.getBody());

        } catch (Exception e) {
            throw new RuntimeException("PhonePe payment initiation failed", e);
        }
    }

    /* =========================================================
       STEP 9.2 — VERIFY PAYMENT (WEBHOOK / REDIRECT)
       ========================================================= */
    @Override
    public void verifyAndConfirmPayment(VerifyPaymentRequest request) {

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if ("SUCCESS".equalsIgnoreCase(request.getState())) {

            booking.setStatus("CONFIRMED");
            bookingRepository.save(booking);

        } else {

            booking.setStatus("CANCELLED");
            bookingRepository.save(booking);
            rollbackService.rollbackInventory(booking);
        }
    }


    /* =========================================================
       UTILITY — SHA256
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

