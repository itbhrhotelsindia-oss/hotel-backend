package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.model.HotelDetails;
import com.example.hotelbackend.repository.HotelDetailsRepository;
import com.example.hotelbackend.service.HotelDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelDetailsServiceImpl implements HotelDetailsService {

    private final HotelDetailsRepository repository;

    public HotelDetailsServiceImpl(HotelDetailsRepository repository) {
        this.repository = repository;
    }

    @Override
    public HotelDetails getByHotelId(String hotelId) {
        return repository.findById(hotelId).orElse(null);
    }

    @Override
    public List<HotelDetails> getAll() {
        return repository.findAll();
    }

    @Override
    public HotelDetails create(HotelDetails hotelDetails) {
        return repository.save(hotelDetails);
    }

    @Override
    public HotelDetails update(String hotelId, HotelDetails hotelDetails) {
        hotelDetails.setHotelId(hotelId);
        return repository.save(hotelDetails);
    }

    @Override
    public void delete(String hotelId) {
        repository.deleteById(hotelId);
    }
}

