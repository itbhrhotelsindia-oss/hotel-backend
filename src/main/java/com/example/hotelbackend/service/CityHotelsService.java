package com.example.hotelbackend.service;

import com.example.hotelbackend.model.CityHotels;
import com.example.hotelbackend.repository.CityHotelsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityHotelsService {

    private final CityHotelsRepository repo;

    public CityHotelsService(CityHotelsRepository repo) {
        this.repo = repo;
    }

    public List<CityHotels> getAll() { return repo.findAll(); }

    public CityHotels getById(String id) { return repo.findById(id).orElse(null); }

    public CityHotels create(CityHotels cityHotels) { return repo.save(cityHotels); }

    public CityHotels update(String id, CityHotels updated) {
        updated.setId(id);
        return repo.save(updated);
    }

    public List<CityHotels> createBulk(List<CityHotels> list) {
        return repo.saveAll(list);
    }


    public void delete(String id) { repo.deleteById(id); }
}

