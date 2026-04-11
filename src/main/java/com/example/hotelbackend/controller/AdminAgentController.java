package com.example.hotelbackend.controller;

import com.example.hotelbackend.dto.agent.CreateAgentRequest;
import com.example.hotelbackend.model.Agent;
import com.example.hotelbackend.repository.AgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/agents")
@RequiredArgsConstructor
public class AdminAgentController {

    private final AgentRepository agentRepo;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();


    /* =====================================================
       1️⃣ CREATE AGENT
       ===================================================== */

    @PostMapping
    public Agent createAgent(
            @RequestBody CreateAgentRequest request
    ) {

        agentRepo.findByUsername(request.getUsername())
                .ifPresent(a -> {
                    throw new RuntimeException(
                            "Username already exists"
                    );
                });

        String hashedPassword =
                encoder.encode(request.getPassword());

        Agent agent = Agent.builder()
                .username(request.getUsername())
                .password(hashedPassword)
                .role("AGENT")

                .companyName(request.getCompanyName())
                .contactPerson(request.getContactPerson())
                .email(request.getEmail())
                .mobileNumber(request.getMobileNumber())

                .hotelIds(request.getHotelIds())
                .active(true)

                .build();

        return agentRepo.save(agent);
    }



    /* =====================================================
       2️⃣ GET ALL AGENTS
       ===================================================== */

    @GetMapping
    public List<Agent> getAllAgents() {

        return agentRepo.findAll();

    }



    /* =====================================================
       3️⃣ DELETE AGENT
       ===================================================== */

    @DeleteMapping("/{id}")
    public String deleteAgent(
            @PathVariable String id
    ) {

        agentRepo.deleteById(id);

        return "Agent deleted successfully";

    }

}