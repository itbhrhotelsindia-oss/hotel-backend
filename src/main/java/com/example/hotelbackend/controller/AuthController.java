package com.example.hotelbackend.controller;

import com.example.hotelbackend.config.JwtUtil;
import com.example.hotelbackend.dto.auth.LoginRequest;
import com.example.hotelbackend.dto.auth.LoginResponse;
import com.example.hotelbackend.model.Owner;
import com.example.hotelbackend.repository.OwnerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.hotelbackend.model.Agent;
import com.example.hotelbackend.repository.AgentRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final OwnerRepository ownerRepo;
    private final AgentRepository agentRepo;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(
            OwnerRepository ownerRepo,
            AgentRepository agentRepo,
            JwtUtil jwtUtil
    ) {
        this.ownerRepo = ownerRepo;
        this.agentRepo = agentRepo;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        // 1️⃣ Try Owner Login
        var ownerOpt = ownerRepo.findByUsername(request.getUsername());

        if (ownerOpt.isPresent()) {

            Owner owner = ownerOpt.get();

            if (!encoder.matches(
                    request.getPassword(),
                    owner.getPassword()
            )) {
                throw new RuntimeException("Invalid password");
            }

            if (!owner.isActive()) {
                throw new RuntimeException("Owner inactive");
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

        // 2️⃣ Try Agent Login
        var agentOpt = agentRepo.findByUsername(request.getUsername());

        if (agentOpt.isPresent()) {

            Agent agent = agentOpt.get();

            if (!encoder.matches(
                    request.getPassword(),
                    agent.getPassword()
            )) {
                throw new RuntimeException("Invalid password");
            }

            if (!agent.isActive()) {
                throw new RuntimeException("Agent inactive");
            }

            String token = jwtUtil.generateToken(
                    agent.getId(),
                    agent.getUsername(),
                    agent.getRole()
            );

            return new LoginResponse(
                    token,
                    agent.getId(),
                    agent.getUsername(),
                    agent.getRole()
            );
        }

        throw new RuntimeException("Invalid username");
    }
}

