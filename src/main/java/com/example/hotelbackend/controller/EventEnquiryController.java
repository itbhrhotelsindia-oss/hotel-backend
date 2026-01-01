package com.example.hotelbackend.controller;

import com.example.hotelbackend.model.EventEnquiry;
import com.example.hotelbackend.service.EventEnquiryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events/enquiry")
public class EventEnquiryController {

    private final EventEnquiryService service;

    public EventEnquiryController(EventEnquiryService service) {
        this.service = service;
    }

    // ⭐ SUBMIT ENQUIRY
    @PostMapping
    public ResponseEntity<EventEnquiry> submit(
            @Valid @RequestBody EventEnquiry enquiry) {
        return ResponseEntity.ok(service.submit(enquiry));
    }

    // ⭐ GET ALL (ADMIN)
    @GetMapping
    public ResponseEntity<List<EventEnquiry>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ⭐ GET ONE (ADMIN)
    @GetMapping("/{id}")
    public ResponseEntity<EventEnquiry> getOne(
            @PathVariable String id) {
        return ResponseEntity.ok(service.getOne(id));
    }

    // ⭐ DELETE (ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok("Event enquiry deleted");
    }
}
