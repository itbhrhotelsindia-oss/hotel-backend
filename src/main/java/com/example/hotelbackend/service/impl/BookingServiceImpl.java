package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.dto.booking.*;
import com.example.hotelbackend.model.Booking;
import com.example.hotelbackend.model.CityHotels;
import com.example.hotelbackend.model.Hotel;
import com.example.hotelbackend.repository.BookingRepository;
import com.example.hotelbackend.repository.CityHotelsRepository;
import com.example.hotelbackend.service.*;
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

        // 1️⃣ Availability check
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

        // 2️⃣ Reserve inventory
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

        LocalDateTime now = LocalDateTime.now();

        Booking booking = Booking.builder()
                .bookingId(generateBookingId())
                .hotelId(request.getHotelId())
                .roomTypeId(request.getRoomTypeId())
                .checkIn(LocalDate.parse(request.getCheckIn()))
                .checkOut(LocalDate.parse(request.getCheckOut()))
                .rooms(request.getRooms())
                .nights(nights)

                .pricingType(request.getPricingType())
                .payMode(request.getPayMode())
                .pricePerNight(request.getPricePerNight())
                .totalAmount(request.getTotalAmount())

                .status("PENDING")

                // 🔥 IMPORTANT
                .createdAt(now)
                .paymentExpiresAt(
                        "PAY_NOW".equalsIgnoreCase(
                                request.getPayMode().replace(" ", "_")
                        )
                                ? now.plusMinutes(5)
                                : null
                )


                .guestName(request.getGuestName())
                .guestEmail(request.getGuestEmail())
                .guestPhone(request.getGuestPhone())
                .build();


        Booking savedBooking = bookingRepository.save(booking);

        // 4️⃣ Send booking emails
        sendBookingCreatedEmails(savedBooking);

        return savedBooking;
    }

    /* ===========================
       📧 BOOKING CREATED EMAILS
       =========================== */
    private void sendBookingCreatedEmails(Booking booking) {

        String hotelName = getHotelNameByHotelId(booking.getHotelId());

        // ---------- Guest Email ----------
        String guestSubject =
                "Booking Created | " + hotelName + " | " + booking.getBookingId();

        String paymentMessage =
                "PAY_NOW".equalsIgnoreCase(booking.getPayMode())
                        ? "Please proceed with payment to confirm your booking."
                        : "Please pay the booking amount at the hotel during check-in.";

        String guestBody =
                "Dear " + booking.getGuestName() + ",\n\n" +
                        "Your booking has been created successfully.\n\n" +
                        "Booking ID: " + booking.getBookingId() + "\n" +
                        "Hotel: " + hotelName + "\n" +
                        "Check-in: " + booking.getCheckIn() + "\n" +
                        "Check-out: " + booking.getCheckOut() + "\n" +
                        "Rooms: " + booking.getRooms() + "\n\n" +
                        "Booking Type: " + formatPricingType(booking.getPricingType()) + "\n" +
                        "Total Amount: ₹" + booking.getTotalAmount() + "\n\n" +
                        "Payment Status: PAYMENT PENDING\n" +
                        paymentMessage + "\n\n" +
                        "Thank you for choosing BHR Hotels India.\n";

        emailService.sendEmail(
                booking.getGuestEmail(),
                guestSubject,
                guestBody
        );

        // ---------- Owner Email ----------
        String ownerSubject =
                "New Booking | " + hotelName + " | " + booking.getBookingId();

        String ownerBody =
                "New booking received:\n\n" +
                        "Booking ID: " + booking.getBookingId() + "\n" +
                        "Guest Name: " + booking.getGuestName() + "\n" +
                        "Phone: " + booking.getGuestPhone() + "\n" +
                        "Check-in: " + booking.getCheckIn() + "\n" +
                        "Check-out: " + booking.getCheckOut() + "\n" +
                        "Rooms: " + booking.getRooms() + "\n" +
                        "Booking Type: " + formatPricingType(booking.getPricingType()) + "\n" +
                        "Payment Mode: " + booking.getPayMode() + "\n" +
                        "Total Amount: ₹" + booking.getTotalAmount() + "\n" +
                        "Status: PAYMENT PENDING";

        emailService.notifyOwner(ownerSubject, ownerBody);
    }

    /* ===========================
       📧 WELCOME EMAIL (PAYMENT SUCCESS)
       =========================== */
    public void sendWelcomeEmail(Booking booking) {

        String hotelName = getHotelNameByHotelId(booking.getHotelId());

        String subject =
                "Welcome to " + hotelName + " | Booking Confirmed | " + booking.getBookingId();

        String body =
                "Dear " + booking.getGuestName() + ",\n\n" +
                        "Greetings from " + hotelName + "!\n\n" +
                        "We are delighted to confirm your reservation with us.\n\n" +
                        "Booking ID: " + booking.getBookingId() + "\n" +
                        "Check-in: " + booking.getCheckIn() + "\n" +
                        "Check-out: " + booking.getCheckOut() + "\n" +
                        "Rooms: " + booking.getRooms() + "\n" +
                        "Booking Type: " + formatPricingType(booking.getPricingType()) + "\n\n" +
                        "We look forward to welcoming you and ensuring you have a memorable stay.\n\n" +
                        "Warm regards,\n" +
                        hotelName + "\n" +
                        "BHR Hotels India";

        emailService.sendEmail(
                booking.getGuestEmail(),
                subject,
                body
        );
    }

    /* ===========================
       🔧 HELPERS
       =========================== */
    private String generateBookingId() {
        return "BHR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String formatPricingType(String type) {
        return switch (type) {
            case "ROOM_WITH_BREAKFAST" -> "Room with Breakfast";
            case "ROOM_WITH_MEALS" -> "Room with Meals";
            default -> "Room Only";
        };
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
}
