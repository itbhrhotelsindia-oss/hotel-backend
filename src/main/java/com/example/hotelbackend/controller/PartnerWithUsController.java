package com.example.hotelbackend.controller;

import com.example.hotelbackend.model.PartnerWithUsEnquiry;
import com.example.hotelbackend.service.PartnerWithUsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partner-with-us")
public class PartnerWithUsController {

    private final PartnerWithUsService service;

    public PartnerWithUsController(PartnerWithUsService service) {
        this.service = service;
    }

    // ⭐ FRONTEND SUBMIT
    @PostMapping
    public ResponseEntity<PartnerWithUsEnquiry> submit(
            @Valid @RequestBody PartnerWithUsEnquiry enquiry) {
        return ResponseEntity.ok(service.submit(enquiry));
    }

    // ⭐ ADMIN - GET ALL
    @GetMapping
    public ResponseEntity<List<PartnerWithUsEnquiry>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ⭐ ADMIN - GET ONE
    @GetMapping("/{id}")
    public ResponseEntity<PartnerWithUsEnquiry> getOne(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ⭐ ADMIN - DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok("Partner enquiry deleted");
    }
}

