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

    // POST new Wedding page JSON
    @PostMapping("/")
    public ResponseEntity<WeddingPage> create(@RequestBody WeddingPage page) {
        return ResponseEntity.ok(service.save(page));
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

