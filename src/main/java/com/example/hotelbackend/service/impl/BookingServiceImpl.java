package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.dto.booking.CreateBookingRequest;
import com.example.hotelbackend.dto.booking.CheckAvailabilityRequest;
import com.example.hotelbackend.dto.booking.AvailabilityResponse;
import com.example.hotelbackend.model.Booking;
import com.example.hotelbackend.service.BookingAvailabilityService;
import com.example.hotelbackend.service.BookingService;
import com.example.hotelbackend.service.InventoryReservationService;
import com.example.hotelbackend.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingAvailabilityService availabilityService;
    private final InventoryReservationService reservationService;
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(
            BookingAvailabilityService availabilityService,
            InventoryReservationService reservationService,
            BookingRepository bookingRepository
    ) {
        this.availabilityService = availabilityService;
        this.reservationService = reservationService;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking createPendingBooking(CreateBookingRequest request) {

        // 1️⃣ Check availability (STEP 6)
        CheckAvailabilityRequest availabilityRequest = new CheckAvailabilityRequest();
        availabilityRequest.setHotelId(request.getHotelId());
        availabilityRequest.setRoomTypeId(request.getRoomTypeId());
        availabilityRequest.setCheckIn(request.getCheckIn());
        availabilityRequest.setCheckOut(request.getCheckOut());
        availabilityRequest.setRoomsRequested(request.getRooms());

        Object availabilityResult =
                availabilityService.checkAvailability(availabilityRequest);

        if (!(availabilityResult instanceof AvailabilityResponse)) {
            throw new RuntimeException("Rooms not available");
        }

        AvailabilityResponse availability =
                (AvailabilityResponse) availabilityResult;

        // 2️⃣ Reserve inventory (STEP 7)
        reservationService.reserveInventory(
                request.getHotelId(),
                request.getRoomTypeId(),
                LocalDate.parse(request.getCheckIn()),
                LocalDate.parse(request.getCheckOut()),
                request.getRooms()
        );

        // 3️⃣ Create booking (STEP 8)
        int nights = (int) ChronoUnit.DAYS.between(
                LocalDate.parse(request.getCheckIn()),
                LocalDate.parse(request.getCheckOut())
        );

        Booking booking = Booking.builder()
                .bookingId(generateBookingId())
                .hotelId(request.getHotelId())
                .roomTypeId(request.getRoomTypeId())
                .checkIn(LocalDate.parse(request.getCheckIn()))
                .checkOut(LocalDate.parse(request.getCheckOut()))
                .rooms(request.getRooms())
                .nights(nights)
                .totalAmount(availability.getTotalAmount())
                .status("PENDING")
                .guestName(request.getGuestName())
                .guestEmail(request.getGuestEmail())
                .guestPhone(request.getGuestPhone())
                .createdAt(LocalDateTime.now())
                .build();

        return bookingRepository.save(booking);
    }

    private String generateBookingId() {
        return "BHR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

