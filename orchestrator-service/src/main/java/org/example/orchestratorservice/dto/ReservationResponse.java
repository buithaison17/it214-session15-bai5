package org.example.orchestratorservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    private String bookingId;
    private String concertCode;
    private boolean success;
    private List<String> seatNumbers;
    private String message;
}
