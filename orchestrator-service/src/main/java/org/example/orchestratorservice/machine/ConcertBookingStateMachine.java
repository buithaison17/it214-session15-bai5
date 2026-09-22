package org.example.orchestratorservice.machine;

import org.example.orchestratorservice.dto.BookingRequest;
import org.example.orchestratorservice.entity.BookingTransaction;

public interface ConcertBookingStateMachine {
    BookingTransaction process(BookingRequest request);
}
