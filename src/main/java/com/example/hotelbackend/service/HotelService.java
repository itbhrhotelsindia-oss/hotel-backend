package com.example.hotelbackend.service;

import com.example.hotelbackend.model.Hotel;
import com.example.hotelbackend.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public Hotel create(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Optional<Hotel> getById(String id) {
        return hotelRepository.findById(id);
    }

    public List<Hotel> getAll() {
        return hotelRepository.findAll();
    }

    public List<Hotel> findByCity(String city) {
        return hotelRepository.findByCityIgnoreCase(city);
    }

    public Hotel update(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public void delete(String id) {
        hotelRepository.deleteById(id);
    }
}
