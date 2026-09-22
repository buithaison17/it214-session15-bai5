package org.example.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.paymentservice.dto.PaymentRequest;
import org.example.paymentservice.dto.PaymentResponse;
import org.example.paymentservice.service.PaymentProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentProcessingService paymentProcessingService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(
            @RequestBody PaymentRequest request
    ) {
        PaymentResponse response =
                paymentProcessingService.processPayment(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refund/{bookingId}")
    public ResponseEntity<PaymentResponse> refundPayment(
            @PathVariable String bookingId
    ) {
        PaymentResponse response =
                paymentProcessingService.refundPayment(bookingId);
        return ResponseEntity.ok(response);
    }
}
