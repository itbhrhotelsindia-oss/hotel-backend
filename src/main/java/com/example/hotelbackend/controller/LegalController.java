package com.example.hotelbackend.controller;
import com.example.hotelbackend.dto.legal.LegalPageResponse;
import com.example.hotelbackend.service.LegalService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/legal")
@CrossOrigin // allow frontend & payment gateways
public class LegalController {

    private final LegalService legalService;

    public LegalController(LegalService legalService) {
        this.legalService = legalService;
    }

    @GetMapping("/terms-and-conditions")
    public LegalPageResponse termsAndConditions() {
        return legalService.getTermsAndConditions();
    }

    @GetMapping("/privacy-policy")
    public LegalPageResponse privacyPolicy() {
        return legalService.getPrivacyPolicy();
    }

    @GetMapping("/refund-and-cancellation-policy")
    public LegalPageResponse refundPolicy() {
        return legalService.getRefundPolicy();
    }
}

