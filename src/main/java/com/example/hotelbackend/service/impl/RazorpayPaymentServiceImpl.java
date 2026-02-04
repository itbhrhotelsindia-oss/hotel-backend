package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.dto.payment.VerifyPaymentRequest;
import com.example.hotelbackend.model.Booking;
import com.example.hotelbackend.repository.BookingRepository;
import com.example.hotelbackend.service.InventoryRollbackService;
import com.example.hotelbackend.service.PaymentService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayPaymentServiceImpl implements PaymentService {

    private final BookingRepository bookingRepository;
    private final InventoryRollbackService rollbackService;

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public RazorpayPaymentServiceImpl(
            BookingRepository bookingRepository,
            InventoryRollbackService rollbackService
    ) {
        this.bookingRepository = bookingRepository;
        this.rollbackService = rollbackService;
    }

    /* =========================
       CREATE RAZORPAY ORDER
       ========================= */
    @Override
    public JSONObject createOrder(String bookingId) {

        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found for bookingId=" + bookingId)
                );

        if (!"PENDING".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException("Payment not allowed for current booking status");
        }

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int) (booking.getTotalAmount() * 100)); // paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", bookingId);

        JSONObject notes = new JSONObject();
        notes.put("bookingId", bookingId);
        orderRequest.put("notes", notes);

        // Frontend will send this order to Razorpay Checkout
        return orderRequest;
    }

    /* =========================
       VERIFY PAYMENT (WEBHOOK)
       ========================= */
    @Override
    public void verifyAndConfirmPayment(VerifyPaymentRequest request) {

        Booking booking = bookingRepository.findByBookingId(request.getBookingId())
                .orElseThrow(() ->
                        new RuntimeException("Booking not found for bookingId=" + request.getBookingId())
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

