package com.example.hotelbackend.controller;
import com.example.hotelbackend.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/public/payments/razorpay")
public class RazorpayPaymentController {

    private final PaymentService paymentService;

    public RazorpayPaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // STEP 1: Create Razorpay Order
    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@RequestParam String bookingId) {
        return ResponseEntity.ok(
                paymentService.createOrder(bookingId)
        );
    }
}
