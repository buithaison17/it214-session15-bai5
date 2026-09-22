package org.example.concertservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReservationRequest {
    private String bookingId;
    private String concertCode;
    private String customerId;
    private int ticketQuantity;
}
