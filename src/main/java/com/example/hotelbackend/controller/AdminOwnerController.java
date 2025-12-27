package com.example.hotelbackend.controller;

import com.example.hotelbackend.dto.owner.CreateOwnerRequest;
import com.example.hotelbackend.dto.owner.CreateOwnerResponse;
import com.example.hotelbackend.model.Owner;
import com.example.hotelbackend.repository.OwnerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/owners")
public class AdminOwnerController {

    private final OwnerRepository ownerRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AdminOwnerController(OwnerRepository ownerRepo) {
        this.ownerRepo = ownerRepo;
    }

    @PostMapping
    public CreateOwnerResponse createOwner(@RequestBody CreateOwnerRequest request) {

        // 🔐 Check username uniqueness
        ownerRepo.findByUsername(request.getUsername())
                .ifPresent(o -> {
                    throw new RuntimeException("Username already exists");
                });

        // 🔐 Hash password
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
}

