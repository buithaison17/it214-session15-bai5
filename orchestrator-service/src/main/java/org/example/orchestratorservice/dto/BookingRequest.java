package org.example.orchestratorservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private String bookingId;
    private String concertCode;
    private String customerId;
    private String customerEmail;
    private int ticketQuantity;
    private double amount;
}