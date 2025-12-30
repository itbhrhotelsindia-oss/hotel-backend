package com.example.hotelbackend.service;

import com.example.hotelbackend.model.WeddingEnquiry;
import com.example.hotelbackend.repository.WeddingEnquiryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WeddingEnquiryService {

    private final WeddingEnquiryRepository repository;

    public WeddingEnquiryService(WeddingEnquiryRepository repository) {
        this.repository = repository;
    }

    // SAVE ENQUIRY
    public WeddingEnquiry submit(WeddingEnquiry enquiry) {
        enquiry.setPage("WEDDING");
        enquiry.setCreatedAt(LocalDateTime.now());
        return repository.save(enquiry);
    }

    // ADMIN APIs
    public List<WeddingEnquiry> getAll() {
        return repository.findAll();
    }

    public WeddingEnquiry getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}

