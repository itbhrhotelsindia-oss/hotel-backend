package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.dto.booking.CreateBookingRequest;
import com.example.hotelbackend.dto.booking.CheckAvailabilityRequest;
import com.example.hotelbackend.dto.booking.AvailabilityResponse;
import com.example.hotelbackend.model.Booking;
import com.example.hotelbackend.model.CityHotels;
import com.example.hotelbackend.model.Hotel;
import com.example.hotelbackend.repository.BookingRepository;
import com.example.hotelbackend.repository.CityHotelsRepository;
import com.example.hotelbackend.service.BookingAvailabilityService;
import com.example.hotelbackend.service.BookingService;
import com.example.hotelbackend.service.InventoryReservationService;
import com.example.hotelbackend.service.EmailService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingAvailabilityService availabilityService;
    private final InventoryReservationService reservationService;
    private final BookingRepository bookingRepository;
    private final EmailService emailService;
    private final CityHotelsRepository cityHotelsRepository;

    public BookingServiceImpl(
            BookingAvailabilityService availabilityService,
            InventoryReservationService reservationService,
            BookingRepository bookingRepository,
            EmailService emailService,
            CityHotelsRepository cityHotelsRepository
    ) {
        this.availabilityService = availabilityService;
        this.reservationService = reservationService;
        this.bookingRepository = bookingRepository;
        this.emailService = emailService;
        this.cityHotelsRepository = cityHotelsRepository;
    }

    @Override
    public Booking createPendingBooking(CreateBookingRequest request) {

        /* =========================
           1️⃣ Availability check
           ========================= */
        CheckAvailabilityRequest availabilityRequest = new CheckAvailabilityRequest();
        availabilityRequest.setHotelId(request.getHotelId());
        availabilityRequest.setRoomTypeId(request.getRoomTypeId());
        availabilityRequest.setCheckIn(request.getCheckIn());
        availabilityRequest.setCheckOut(request.getCheckOut());
        availabilityRequest.setRoomsRequested(request.getRooms());

        Object result = availabilityService.checkAvailability(availabilityRequest);
        if (!(result instanceof AvailabilityResponse)) {
            throw new RuntimeException("Rooms not available");
        }

        /* =========================
           2️⃣ Reserve inventory
           ========================= */
        reservationService.reserveInventory(
                request.getHotelId(),
                request.getRoomTypeId(),
                LocalDate.parse(request.getCheckIn()),
                LocalDate.parse(request.getCheckOut()),
                request.getRooms()
        );

        int nights = (int) ChronoUnit.DAYS.between(
                LocalDate.parse(request.getCheckIn()),
                LocalDate.parse(request.getCheckOut())
        );

        /* =========================
           3️⃣ Create booking
           ========================= */
        Booking booking = Booking.builder()
                .bookingId(generateBookingId())
                .hotelId(request.getHotelId())
                .roomTypeId(request.getRoomTypeId())
                .checkIn(LocalDate.parse(request.getCheckIn()))
                .checkOut(LocalDate.parse(request.getCheckOut()))
                .rooms(request.getRooms())
                .nights(nights)

                // ✅ Pricing info (from frontend selection)
                .pricingType(request.getPricingType())
                .payMode(request.getPayMode())
                .pricePerNight(request.getPricePerNight())
                .totalAmount(request.getTotalAmount())

                .status("PENDING")
                .guestName(request.getGuestName())
                .guestEmail(request.getGuestEmail())
                .guestPhone(request.getGuestPhone())
                .createdAt(LocalDateTime.now())
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        /* =========================
           4️⃣ Send Emails (NON-BLOCKING)
           ========================= */
        sendBookingEmails(savedBooking);

        return savedBooking;
    }

    /* =========================
       EMAIL LOGIC
       ========================= */
    private void sendBookingEmails(Booking booking) {

        try {
            String hotelName = getHotelNameByHotelId(booking.getHotelId());

            boolean isPayNow = "PAY_NOW".equalsIgnoreCase(booking.getPayMode());

            /* ---------- Guest Email ---------- */
            String guestSubject =
                    "Booking Created | " + hotelName + " | " + booking.getBookingId();

            String guestPaymentLine = isPayNow
                    ? "Payment Status: PAYMENT PENDING\nPlease proceed with payment to confirm your booking."
                    : "Payment Mode: PAY AT HOTEL\nPlease pay the below amount at the hotel during check-in.";

            String guestBody =
                    "Dear " + booking.getGuestName() + ",\n\n" +
                            "Your booking has been created successfully.\n\n" +
                            "Booking ID: " + booking.getBookingId() + "\n" +
                            "Hotel: " + hotelName + "\n" +
                            "Check-in: " + booking.getCheckIn() + "\n" +
                            "Check-out: " + booking.getCheckOut() + "\n" +
                            "Rooms: " + booking.getRooms() + "\n\n" +

                            "Booking Type: " + formatPricingType(booking.getPricingType()) + "\n" +
                            "Payment Mode: " + formatPayMode(booking.getPayMode()) + "\n" +
                            "Price per Night: ₹" + booking.getPricePerNight() + "\n" +
                            "Total Amount: ₹" + booking.getTotalAmount() + "\n\n" +

                            guestPaymentLine + "\n\n" +
                            "Status: PENDING\n\n" +
                            "Thank you for choosing BHR Hotels India.";

            emailService.sendEmail(
                    booking.getGuestEmail(),
                    guestSubject,
                    guestBody
            );

            /* ---------- Owner Email ---------- */
            String ownerSubject =
                    "New Booking | " + hotelName + " | " + booking.getBookingId();

            String ownerPaymentLine = isPayNow
                    ? "Payment Status: PAYMENT PENDING (Guest will pay online)"
                    : "Payment Mode: PAY AT HOTEL (Guest will pay at hotel)";

            String ownerBody =
                    "New booking received:\n\n" +
                            "Booking ID: " + booking.getBookingId() + "\n" +
                            "Guest Name: " + booking.getGuestName() + "\n" +
                            "Guest Phone: " + booking.getGuestPhone() + "\n" +
                            "Guest Email: " + booking.getGuestEmail() + "\n\n" +

                            "Booking Type: " + formatPricingType(booking.getPricingType()) + "\n" +
                            "Payment Mode: " + formatPayMode(booking.getPayMode()) + "\n" +
                            "Price per Night: ₹" + booking.getPricePerNight() + "\n" +
                            "Total Amount: ₹" + booking.getTotalAmount() + "\n\n" +

                            ownerPaymentLine;

            emailService.notifyOwner(ownerSubject, ownerBody);

        } catch (Exception e) {
            // ❗ Email failure must NOT break booking flow
            System.err.println("Email failed for booking "
                    + booking.getBookingId() + ": " + e.getMessage());
        }
    }

    /* =========================
       HELPERS
       ========================= */
    private String generateBookingId() {
        return "BHR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
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
        return hotelId;
    }

    private String formatPricingType(String type) {
        return type.replace("_", " ");
    }

    private String formatPayMode(String mode) {
        return mode.replace("_", " ");
    }
}
