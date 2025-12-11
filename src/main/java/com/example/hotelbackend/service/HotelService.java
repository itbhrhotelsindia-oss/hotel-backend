package com.example.hotelbackend.service;

import com.example.hotelbackend.model.Hotel;
import com.example.hotelbackend.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService {

    private final HotelRepository repo;

    public HotelService(HotelRepository repo) {
        this.repo = repo;
    }

    // 1️⃣ ADD HOTEL
    public Hotel addHotel(Hotel hotel) {
        return repo.save(hotel);
    }

    public List<Hotel> addHotels(List<Hotel> hotels) {
        return repo.saveAll(hotels);
    }

    // 2️⃣ GET ALL CITIES
    public List<String> getAllCities() {
        return repo.findAll()
                .stream()
                .map(Hotel::getCity)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    // 3️⃣ GET HOTELS BY CITY
    public List<Hotel> getHotelsByCity(String city) {
        return repo.findByCityIgnoreCase(city);
    }

    // 4️⃣ GET ALL HOTELS (optional)
    public List<Hotel> getAllHotels() {
        return repo.findAll();
    }
}
