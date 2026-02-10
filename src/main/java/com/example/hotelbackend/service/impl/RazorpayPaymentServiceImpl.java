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

    /* =========================
       CREATE RAZORPAY ORDER
       ========================= */
    @Override
    public Map<String, Object> createOrder(String bookingId) {

        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found: " + bookingId)
                );

        if (!"PENDING".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException("Payment not allowed for booking status");
        }

        int amountInPaise = (int) booking.getTotalAmount() * 100;

        try {
            JSONObject options = new JSONObject();
            options.put("amount", amountInPaise);
            options.put("currency", "INR");
            options.put("receipt", bookingId);
            options.put("payment_capture", 1);

            Order order = razorpayClient.orders.create(options);

            booking.setRazorpayOrderId(order.get("id").toString());
            bookingRepository.save(booking);

            Map<String, Object> response = new HashMap<>();
            response.put("orderId", booking.getRazorpayOrderId());
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

            sendWelcomeEmailToGuest(booking);
            sendPaymentSuccessEmailToOwner(booking);

        } else {

            booking.setStatus("CANCELLED");
            bookingRepository.save(booking);
            rollbackService.rollbackInventory(booking);

            sendPaymentFailureEmails(booking);
        }
    }

    /* =========================
       ✅ GUEST WELCOME EMAIL
       ========================= */
    private void sendWelcomeEmailToGuest(Booking booking) {

        String hotelName = getHotelNameByHotelId(booking.getHotelId());

        String subject =
                "Welcome to " + hotelName +
                        " | Booking Confirmed | " + booking.getBookingId();

        String body =
                "Dear " + booking.getGuestName() + ",\n\n" +
                        "Warm greetings from " + hotelName + "!\n\n" +
                        "We are delighted to confirm your reservation and look forward " +
                        "to welcoming you for a memorable stay.\n\n" +

                        "📌 Booking Details\n" +
                        "Booking ID: " + booking.getBookingId() + "\n" +
                        "Hotel: " + hotelName + "\n" +
                        "Stay Type: " + booking.getPricingType().replace("_", " ") + "\n" +
                        "Check-in: " + booking.getCheckIn() + "\n" +
                        "Check-out: " + booking.getCheckOut() + "\n" +
                        "Rooms: " + booking.getRooms() + "\n\n" +

                        "If you need any assistance before arrival, " +
                        "please feel free to reach out to us.\n\n" +

                        "We wish you a pleasant and comfortable stay.\n\n" +
                        "Warm regards,\n" +
                        hotelName + "\n" +
                        "BHR Hotels India";

        emailService.sendEmail(
                booking.getGuestEmail(),
                subject,
                body
        );
    }

    /* =========================
       OWNER PAYMENT EMAIL
       ========================= */
    private void sendPaymentSuccessEmailToOwner(Booking booking) {

        String hotelName = getHotelNameByHotelId(booking.getHotelId());

        String subject =
                "Payment Received | Booking Confirmed | " +
                        hotelName + " | " + booking.getBookingId();

        String body =
                "Payment successfully received for booking.\n\n" +
                        "Booking ID: " + booking.getBookingId() + "\n" +
                        "Guest Name: " + booking.getGuestName() + "\n" +
                        "Hotel: " + hotelName + "\n" +
                        "Booking Type: " + booking.getPricingType() + "\n" +
                        "Total Amount: ₹" + booking.getTotalAmount() + "\n" +
                        "Status: CONFIRMED";

        emailService.notifyOwner(subject, body);
    }

    /* =========================
       PAYMENT FAILURE EMAILS
       ========================= */
    private void sendPaymentFailureEmails(Booking booking) {

        String hotelName = getHotelNameByHotelId(booking.getHotelId());

        String subject =
                "Payment Failed | Booking Cancelled | " +
                        hotelName + " | " + booking.getBookingId();

        String body =
                "Unfortunately, payment failed for the following booking:\n\n" +
                        "Booking ID: " + booking.getBookingId() + "\n" +
                        "Hotel: " + hotelName + "\n" +
                        "Status: CANCELLED";

        emailService.sendEmail(
                booking.getGuestEmail(),
                subject,
                body
        );

        emailService.notifyOwner(subject, body);
    }

    /* =========================
       HOTEL NAME RESOLVER
       ========================= */
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

        return hotelId; // safe fallback
    }
}
