package com.example.hotelbackend.controller;

import com.example.hotelbackend.dto.owner.CreateOwnerRequest;
import com.example.hotelbackend.dto.owner.CreateOwnerResponse;
import com.example.hotelbackend.model.Owner;
import com.example.hotelbackend.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/owners")
@RequiredArgsConstructor
public class AdminOwnerController {

    private final OwnerRepository ownerRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /* =========================================================
       1️⃣ CREATE OWNER
       ========================================================= */
    @PostMapping
    public CreateOwnerResponse createOwner(@RequestBody CreateOwnerRequest request) {

        ownerRepo.findByUsername(request.getUsername())
                .ifPresent(o -> {
                    throw new RuntimeException("Username already exists");
                });

        String hashedPassword = encoder.encode(request.getPassword());

        Owner owner = Owner.builder()
                .username(request.getUsername())
                .password(hashedPassword)
                .role("OWNER")
                .hotelIds(request.getHotelIds())
                .active(true)
                .build();

        Owner saved = ownerRepo.save(owner);

        return new CreateOwnerResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getHotelIds(),
                "Owner created successfully"
        );
    }

    /* =========================================================
       2️⃣ GET ALL OWNERS
       ========================================================= */
    @GetMapping
    public List<Owner> getAllOwners() {
        return ownerRepo.findAll();
    }

    /* =========================================================
       3️⃣ GET OWNER BY ID
       ========================================================= */
    @GetMapping("/{id}")
    public Owner getOwnerById(@PathVariable String id) {
        return ownerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
    }

    /* =========================================================
       4️⃣ UPDATE OWNER
       ========================================================= */
    @PutMapping("/{id}")
    public Owner updateOwner(
            @PathVariable String id,
            @RequestBody CreateOwnerRequest request
    ) {

        Owner owner = ownerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        owner.setUsername(request.getUsername());
        owner.setHotelIds(request.getHotelIds());

        // Update password only if provided
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            owner.setPassword(encoder.encode(request.getPassword()));
        }

        return ownerRepo.save(owner);
    }

    /* =========================================================
       5️⃣ ACTIVATE / DEACTIVATE OWNER
       ========================================================= */
    @PatchMapping("/{id}/status")
    public Owner toggleOwnerStatus(
            @PathVariable String id,
            @RequestParam boolean active
    ) {

        Owner owner = ownerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        owner.setActive(active);

        return ownerRepo.save(owner);
    }

    /* =========================================================
       6️⃣ DELETE OWNER
       ========================================================= */
    @DeleteMapping("/{id}")
    public String deleteOwner(@PathVariable String id) {

        ownerRepo.deleteById(id);

        return "Owner deleted successfully";
    }
}
