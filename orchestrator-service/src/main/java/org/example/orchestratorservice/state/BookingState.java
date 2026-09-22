package org.example.orchestratorservice.state;

public enum BookingState {
    INITIATED,
    PAYMENT_PENDING,
    PAYMENT_COMPLETED,
    SEAT_RESERVING,
    BOOKING_CONFIRMED,
    CANCELLED
}
