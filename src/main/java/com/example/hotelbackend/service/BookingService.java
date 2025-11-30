package com.example.hotelbackend.service;

import com.example.hotelbackend.model.Booking;
import com.example.hotelbackend.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking create(Booking booking) {
        booking.setStatus("SCHEDULED");
        return bookingRepository.save(booking);
    }

    public Optional<Booking> getById(String id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getByUser(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getByHotel(String hotelId) {
        return bookingRepository.findByHotelId(hotelId);
    }

    public Booking updateStatus(String id, String status) {
        Booking b = bookingRepository.findById(id).orElseThrow();
        b.setStatus(status);
        return bookingRepository.save(b);
    }

    public void cancel(String id) {
        Booking b = bookingRepository.findById(id).orElseThrow();
        b.setStatus("CANCELLED");
        bookingRepository.save(b);
    }
}
