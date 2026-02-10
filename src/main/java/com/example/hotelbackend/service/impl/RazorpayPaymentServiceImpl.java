package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.dto.payment.VerifyPaymentRequest;
import com.example.hotelbackend.model.Booking;
import com.example.hotelbackend.model.CityHotels;
import com.example.hotelbackend.model.Hotel;
import com.example.hotelbackend.repository.BookingRepository;
import com.example.hotelbackend.repository.CityHotelsRepository;
import com.example.hotelbackend.service.EmailService;
import com.example.hotelbackend.service.InventoryRollbackService;
import com.example.hotelbackend.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RazorpayPaymentServiceImpl implements PaymentService {

    private final BookingRepository bookingRepository;
    private final InventoryRollbackService rollbackService;
    private final RazorpayClient razorpayClient;
    private final String keyId;
    private final EmailService emailService;
    private final CityHotelsRepository cityHotelsRepository;


    public RazorpayPaymentServiceImpl(
            BookingRepository bookingRepository,
            InventoryRollbackService rollbackService,
            EmailService emailService,
            CityHotelsRepository cityHotelsRepository,
            @Value("${razorpay.key.id}") String keyId,
            @Value("${razorpay.key.secret}") String keySecret
    ) throws Exception {
        this.bookingRepository = bookingRepository;
        this.rollbackService = rollbackService;
        this.emailService = emailService;
        this.cityHotelsRepository = cityHotelsRepository;
        this.keyId = keyId;
        this.razorpayClient = new RazorpayClient(keyId, keySecret);
    }


    @Override
    public Map<String, Object> createOrder(String bookingId) {

        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found: " + bookingId)
                );

        if (!"PENDING".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException("Payment not allowed for booking status");
        }

        int amountInPaise = (int)booking.getTotalAmount() * 100;

        try {
            JSONObject options = new JSONObject();
            options.put("amount", amountInPaise);
            options.put("currency", "INR");
            options.put("receipt", bookingId);
            options.put("payment_capture", 1);

            // ✅ 1. Create Razorpay order
            Order order = razorpayClient.orders.create(options);

            String razorpayOrderId = order.get("id").toString();

            // ✅ 2. SAVE orderId in DB (🔥 THIS WAS MISSING)
            booking.setRazorpayOrderId(razorpayOrderId);
            bookingRepository.save(booking);

            // ✅ 3. Send to frontend
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", razorpayOrderId);
            response.put("amount", amountInPaise);
            response.put("currency", "INR");
            response.put("keyId", keyId);

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

        Booking booking = bookingRepository
                .findByRazorpayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() ->
                        new RuntimeException("Booking not found for Razorpay orderId")
                );

        if ("SUCCESS".equalsIgnoreCase(request.getState())) {

            booking.setStatus("CONFIRMED");
            bookingRepository.save(booking);

            sendPaymentSuccessEmails(booking);

        } else {

            booking.setStatus("CANCELLED");
            bookingRepository.save(booking);
            rollbackService.rollbackInventory(booking);

            sendPaymentFailureEmails(booking);
        }
    }

    private void sendPaymentSuccessEmails(Booking booking) {

        String hotelName = getHotelNameByHotelId(booking.getHotelId());

        String subjectGuest =
                "Payment Successful | Booking Confirmed | " +
                        hotelName + " | " + booking.getBookingId();

        String bodyGuest =
                "Dear " + booking.getGuestName() + ",\n\n" +
                        "Your payment has been received successfully.\n\n" +
                        "Booking ID: " + booking.getBookingId() + "\n" +
                        "Hotel: " + hotelName + "\n" +
                        "Booking Type: " + booking.getPricingType() + "\n" +
                        "Payment Mode: Pay Now\n" +
                        "Check-in: " + booking.getCheckIn() + "\n" +
                        "Check-out: " + booking.getCheckOut() + "\n" +
                        "Total Amount: ₹" + booking.getTotalAmount() + "\n\n" +
                        "Status: CONFIRMED\n\n" +
                        "Thank you for choosing BHR Hotels India.";

        emailService.sendEmail(
                booking.getGuestEmail(),
                subjectGuest,
                bodyGuest
        );

        emailService.notifyOwner(
                subjectGuest,
                bodyGuest
        );
    }

    private void sendPaymentFailureEmails(Booking booking) {

        String hotelName = getHotelNameByHotelId(booking.getHotelId());

        String subject =
                "Payment Failed | Booking Cancelled | " +
                        hotelName + " | " + booking.getBookingId();

        String body =
                "Payment failed for booking.\n\n" +
                        "Booking ID: " + booking.getBookingId() + "\n" +
                        "Hotel: " + hotelName + "\n" +
                        "Status: CANCELLED\n";

        emailService.sendEmail(
                booking.getGuestEmail(),
                subject,
                body
        );

        emailService.notifyOwner(subject, body);
    }

    private String getHotelNameByHotelId(String hotelId) {

        List<CityHotels> cities = cityHotelsRepository.findAll();

        for (CityHotels city : cities) {

            if (city.getHotels() == null) continue;

            for (Hotel hotel : city.getHotels()) {

                if (hotelId.equals(hotel.getHotelId())) {
                    return hotel.getName();
                }
            }
        }

        // fallback (never fail email/booking)
        return hotelId;
    }


}
