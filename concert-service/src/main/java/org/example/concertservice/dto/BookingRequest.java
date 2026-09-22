package org.example.concertservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingRequest {
    private String bookingId;
    private String concertCode;
    private String customerId;
    private String customerEmail;
    private int ticketQuantity;
    private long amount;
}
