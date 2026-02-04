package com.example.hotelbackend.dto.rozarpay;

import lombok.Data;

@Data
public class RazorpayOrderRequest {
    private String bookingId;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
}
