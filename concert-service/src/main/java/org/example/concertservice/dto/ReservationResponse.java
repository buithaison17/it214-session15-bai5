package org.example.concertservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReservationResponse {
    private boolean success;
    private String bookingId;
    private String message;
    private List<String> seatNumbers;
}
