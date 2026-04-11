package com.example.hotelbackend.controller;

import com.example.hotelbackend.dto.agent.UpsertAgentPricingRequest;
import com.example.hotelbackend.service.AgentPricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/agent-pricing")
@RequiredArgsConstructor
public class AdminAgentPricingController {

    private final AgentPricingService service;



    /* =====================================================
       UPSERT AGENT PRICE
       ===================================================== */

    @PostMapping("/upsert")
    public String upsertPricing(
            @RequestBody UpsertAgentPricingRequest request
    ) {

        service.upsertAgentPricing(request);

        return "Agent pricing saved successfully";
    }

}