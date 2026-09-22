package org.example.orchestratorservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.orchestratorservice.state.BookingState;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingTransaction {
    private String bookingId;
    private BookingState currentState;
    private boolean paymentCompleted;
    private boolean seatsReserved;
    private String message;
}