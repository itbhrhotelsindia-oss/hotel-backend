package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.model.Booking;
import com.example.hotelbackend.service.BookingEmailService;
import com.example.hotelbackend.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BookingEmailServiceImpl implements BookingEmailService {

    private final EmailService emailService;

    @Value("${app.owner.email}")
    private String ownerEmail;

    public BookingEmailServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    /* ===============================
       BOOKING CREATED (PENDING)
       =============================== */
    @Override
    public void bookingCreated(Booking booking) {

        // 👤 Guest email
        String guestSubject = "Booking Received – Payment Pending";
        String guestBody =
                "Dear " + booking.getGuestName() + ",\n\n" +
                        "Thank you for choosing BHR Hotels.\n\n" +
                        "Your booking has been created successfully and is currently PENDING.\n\n" +
                        "Booking ID: " + booking.getBookingId() + "\n" +
                        "Check-in: " + booking.getCheckIn() + "\n" +
                        "Check-out: " + booking.getCheckOut() + "\n" +
                        "Rooms: " + booking.getRooms() + "\n" +
                        "Total Amount: ₹" + booking.getTotalAmount() + "\n\n" +
                        "Please complete payment to confirm your booking.\n\n" +
                        "Regards,\nBHR Hotels";

        emailService.sendEmail(booking.getGuestEmail(), guestSubject, guestBody);

        // 🏨 Owner email
        String ownerSubject = "New Booking Created (Pending Payment)";
        String ownerBody =
                "A new booking has been created.\n\n" +
                        "Booking ID: " + booking.getBookingId() + "\n" +
                        "Guest Name: " + booking.getGuestName() + "\n" +
                        "Phone: " + booking.getGuestPhone() + "\n" +
                        "Dates: " + booking.getCheckIn() + " → " + booking.getCheckOut() + "\n" +
                        "Rooms: " + booking.getRooms() + "\n" +
                        "Amount: ₹" + booking.getTotalAmount();

        emailService.sendEmail(ownerEmail, ownerSubject, ownerBody);
    }

    /* ===============================
       PAYMENT SUCCESS
       =============================== */
    @Override
    public void bookingConfirmed(Booking booking) {

        // 👤 Guest
        String guestSubject = "Booking Confirmed – BHR Hotels";
        String guestBody =
                "Dear " + booking.getGuestName() + ",\n\n" +
                        "Your payment has been received successfully.\n\n" +
                        "Your booking is now CONFIRMED.\n\n" +
                        "Booking ID: " + booking.getBookingId() + "\n" +
                        "Check-in: " + booking.getCheckIn() + "\n" +
                        "Check-out: " + booking.getCheckOut() + "\n\n" +
                        "We look forward to welcoming you.\n\n" +
                        "Regards,\nBHR Hotels";

        emailService.sendEmail(booking.getGuestEmail(), guestSubject, guestBody);

        // 🏨 Owner
        emailService.sendEmail(
                ownerEmail,
                "Booking Confirmed",
                "Booking confirmed successfully.\n\nBooking ID: " + booking.getBookingId()
        );
    }

    /* ===============================
       PAYMENT FAILED
       =============================== */
    @Override
    public void bookingCancelled(Booking booking) {

        // 👤 Guest
        String guestSubject = "Payment Failed – Booking Cancelled";
        String guestBody =
                "Dear " + booking.getGuestName() + ",\n\n" +
                        "Unfortunately, your payment could not be completed.\n\n" +
                        "Your booking has been CANCELLED.\n\n" +
                        "Booking ID: " + booking.getBookingId() + "\n\n" +
                        "You may retry booking anytime.\n\n" +
                        "Regards,\nBHR Hotels";

        emailService.sendEmail(booking.getGuestEmail(), guestSubject, guestBody);

        // 🏨 Owner
        emailService.sendEmail(
                ownerEmail,
                "Booking Cancelled (Payment Failed)",
                "Booking cancelled due to payment failure.\n\nBooking ID: " + booking.getBookingId()
        );
    }
}

