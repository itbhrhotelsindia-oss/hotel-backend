package com.example.hotelbackend.controller;

import com.example.hotelbackend.model.WeddingEnquiry;
import com.example.hotelbackend.service.WeddingEnquiryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weddings/enquiry")
public class WeddingEnquiryController {

    private final WeddingEnquiryService service;

    public WeddingEnquiryController(WeddingEnquiryService service) {
        this.service = service;
    }

    // ⭐ SUBMIT ENQUIRY (Frontend uses this)
    @PostMapping
    public ResponseEntity<WeddingEnquiry> submit(
            @RequestBody WeddingEnquiry enquiry) {
        return ResponseEntity.ok(service.submit(enquiry));
    }

    // ⭐ ADMIN - GET ALL ENQUIRIES
    @GetMapping
    public ResponseEntity<List<WeddingEnquiry>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ⭐ ADMIN - GET ONE
    @GetMapping("/{id}")
    public ResponseEntity<WeddingEnquiry> getOne(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ⭐ ADMIN - DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok("Enquiry deleted");
    }
}

