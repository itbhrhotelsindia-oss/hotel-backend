package com.example.hotelbackend.controller.dev;

import com.example.hotelbackend.dto.payment.VerifyPaymentRequest;
import com.example.hotelbackend.service.PaymentService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dev/payments/mock")
@Profile("dev")   // ⛔ Automatically disabled in prod
public class MockPaymentController {

    private final PaymentService paymentService;

    public MockPaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /* =========================
       MOCK SUCCESS PAYMENT
       ========================= */

    @PostMapping("/success")
    public ResponseEntity<String> mockSuccess(
            @RequestParam String bookingId
    ) {
        VerifyPaymentRequest request = new VerifyPaymentRequest();
        request.setBookingId(bookingId);
        request.setState("SUCCESS");

        paymentService.verifyAndConfirmPayment(request);

        return ResponseEntity.ok("Mock payment SUCCESS");
    }

    /* =========================
       MOCK FAILED PAYMENT
       ========================= */

    @PostMapping("/failure")
    public ResponseEntity<String> mockFailure(
            @RequestParam String bookingId
    ) {
        VerifyPaymentRequest request = new VerifyPaymentRequest();
        request.setBookingId(bookingId);
        request.setState("FAILED");

        paymentService.verifyAndConfirmPayment(request);

        return ResponseEntity.ok("Mock payment FAILED");
    }
}

