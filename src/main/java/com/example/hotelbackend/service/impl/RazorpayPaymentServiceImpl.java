package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.dto.payment.VerifyPaymentRequest;
import com.example.hotelbackend.model.Booking;
import com.example.hotelbackend.repository.BookingRepository;
import com.example.hotelbackend.service.InventoryRollbackService;
import com.example.hotelbackend.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayPaymentServiceImpl implements PaymentService {

    private final BookingRepository bookingRepository;
    private final InventoryRollbackService rollbackService;
    private final RazorpayClient razorpayClient;
    private final String keyId; // ✅ FIXED

    public RazorpayPaymentServiceImpl(
            BookingRepository bookingRepository,
            InventoryRollbackService rollbackService,
            @Value("${razorpay.key.id}") String keyId,
            @Value("${razorpay.key.secret}") String keySecret
    ) throws Exception {
        this.bookingRepository = bookingRepository;
        this.rollbackService = rollbackService;
        this.keyId = keyId; // ✅ STORE IT
        this.razorpayClient = new RazorpayClient(keyId, keySecret);
    }

    /* =========================
       CREATE RAZORPAY ORDER
       ========================= */
    @Override
    public JSONObject createOrder(String bookingId) {

        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found: " + bookingId)
                );

        if (!"PENDING".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException(
                    "Payment not allowed for status: " + booking.getStatus()
            );
        }

        int amountInPaise = (int) Math.round(booking.getTotalAmount() * 100);

        try {
            JSONObject options = new JSONObject();
            options.put("amount", amountInPaise);
            options.put("currency", "INR");
            options.put("receipt", bookingId);
            options.put("payment_capture", 1);

            Order order = razorpayClient.orders.create(options);

            JSONObject response = new JSONObject();
            response.put("orderId", order.get("id").toString());
            response.put("amount", amountInPaise);
            response.put("currency", "INR");
            response.put("keyId", keyId); // ✅ NOW WORKS
            response.put("bookingId", bookingId);

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Razorpay order creation failed", e);
        }
    }

    /* =========================
       VERIFY PAYMENT (WEBHOOK)
       ========================= */
    @Override
    public void verifyAndConfirmPayment(VerifyPaymentRequest request) {

        Booking booking = bookingRepository.findByBookingId(request.getBookingId())
                .orElseThrow(() ->
                        new RuntimeException("Booking not found")
                );

        if ("SUCCESS".equalsIgnoreCase(request.getState())) {
            booking.setStatus("CONFIRMED");
            bookingRepository.save(booking);
        } else {
            booking.setStatus("CANCELLED");
            bookingRepository.save(booking);
            rollbackService.rollbackInventory(booking);
        }
    }
}
