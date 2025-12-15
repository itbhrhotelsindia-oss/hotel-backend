package com.example.hotelbackend.controller;

import com.example.hotelbackend.model.WeddingPage;
import com.example.hotelbackend.service.WeddingPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weddings")
public class WeddingPageController {

    private final WeddingPageService service;

    public WeddingPageController(WeddingPageService service) {
        this.service = service;
    }

    // GET all (usually only 1)
    @GetMapping("/")
    public ResponseEntity<List<WeddingPage>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET one by ID
    @GetMapping("/{id}")
    public ResponseEntity<WeddingPage> getOne(@PathVariable String id) {
        return ResponseEntity.ok(service.getOne(id));
    }

    @PostMapping("/")
    public ResponseEntity<List<WeddingPage>> create(
            @RequestBody List<WeddingPage> pages) {
        return ResponseEntity.ok(service.saveAll(pages));
    }


    // UPDATE existing
    @PutMapping("/{id}")
    public ResponseEntity<WeddingPage> update(@PathVariable String id,
                                              @RequestBody WeddingPage page) {
        return ResponseEntity.ok(service.update(id, page));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok("Wedding page deleted");
    }
}

