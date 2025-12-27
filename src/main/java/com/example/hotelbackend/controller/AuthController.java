package com.example.hotelbackend.controller;

import com.example.hotelbackend.config.JwtUtil;
import com.example.hotelbackend.dto.auth.LoginRequest;
import com.example.hotelbackend.dto.auth.LoginResponse;
import com.example.hotelbackend.model.Owner;
import com.example.hotelbackend.repository.OwnerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final OwnerRepository ownerRepo;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(OwnerRepository ownerRepo, JwtUtil jwtUtil) {
        this.ownerRepo = ownerRepo;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        Owner owner = ownerRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username"));

        if (!encoder.matches(request.getPassword(), owner.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (!owner.isActive()) {
            throw new RuntimeException("Owner is inactive");
        }

        String token = jwtUtil.generateToken(
                owner.getId(),
                owner.getUsername(),
                owner.getRole()
        );

        return new LoginResponse(
                token,
                owner.getId(),
                owner.getUsername(),
                owner.getRole()
        );
    }
}

