package org.example.orchestratorservice.service;

import lombok.RequiredArgsConstructor;
import org.example.orchestratorservice.dto.BookingRequest;
import org.example.orchestratorservice.entity.BookingTransaction;
import org.example.orchestratorservice.machine.ConcertBookingStateMachine;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingOrchestratorService {
    private final ConcertBookingStateMachine stateMachine;

    public BookingTransaction createBooking(
            BookingRequest request
    ) {
        return stateMachine.process(request);
    }
}
