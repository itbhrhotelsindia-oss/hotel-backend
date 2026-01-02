package com.example.hotelbackend.service;

import com.example.hotelbackend.model.HotelDetails;

import java.util.List;

public interface HotelDetailsService {

    HotelDetails getByHotelId(String hotelId);

    List<HotelDetails> getAll();

    HotelDetails create(HotelDetails hotelDetails);

    HotelDetails update(String hotelId, HotelDetails hotelDetails);

    void delete(String hotelId);
}

