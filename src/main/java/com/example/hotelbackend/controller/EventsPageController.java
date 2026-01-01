package com.example.hotelbackend.controller;

import com.example.hotelbackend.model.EventsPage;
import com.example.hotelbackend.service.EventsPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/events-page")
public class EventsPageController {

    private final EventsPageService service;

    public EventsPageController(EventsPageService service) {
        this.service = service;
    }

    // GET FULL PAGE
    @GetMapping
    public ResponseEntity<EventsPage> getPage() {
        return ResponseEntity.ok(service.getPage());
    }

    // UPDATE FULL PAGE
    @PutMapping
    public ResponseEntity<EventsPage> updatePage(
            @RequestBody EventsPage page) {
        return ResponseEntity.ok(service.updatePage(page));
    }

    /* =====================
       SLIDER
       ===================== */

    @PostMapping("/slider/images")
    public ResponseEntity<EventsPage> addSliderImage(
            @RequestBody Map<String, String> body) {

        String imageUrl = body.get("imageUrl");

        return ResponseEntity.ok(
                service.addSliderImage(imageUrl)
        );
    }

    @DeleteMapping("/slider/images/{index}")
    public ResponseEntity<EventsPage> deleteSliderImage(
            @PathVariable int index) {
        return ResponseEntity.ok(service.deleteSliderImage(index));
    }

    /* =====================
       CATEGORIES
       ===================== */

    @PostMapping("/categories")
    public ResponseEntity<EventsPage> addCategory(
            @RequestBody EventsPage.EventCategory category) {
        return ResponseEntity.ok(service.addCategory(category));
    }

    @PutMapping("/categories/{key}")
    public ResponseEntity<EventsPage> updateCategory(
            @PathVariable String key,
            @RequestBody EventsPage.EventCategory category) {
        return ResponseEntity.ok(service.updateCategory(key, category));
    }

    @DeleteMapping("/categories/{key}")
    public ResponseEntity<EventsPage> deleteCategory(
            @PathVariable String key) {
        return ResponseEntity.ok(service.deleteCategory(key));
    }
}

