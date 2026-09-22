package org.example.orchestratorservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.orchestratorservice.dto.BookingRequest;
import org.example.orchestratorservice.entity.BookingTransaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.orchestratorservice.service.BookingOrchestratorService;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingOrchestratorService bookingOrchestratorService;

    @PostMapping
    public ResponseEntity<BookingTransaction> createBooking(
            @RequestBody BookingRequest request
    ) {
        BookingTransaction result =
                bookingOrchestratorService.createBooking(request);

        return ResponseEntity.ok(result);
    }
}
