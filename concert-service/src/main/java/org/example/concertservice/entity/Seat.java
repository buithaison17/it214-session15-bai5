package org.example.concertservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.concertservice.constants.SeatStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Seat {
    private String seatNumber;
    private String concertCode;
    private SeatStatus status;
    private String reservedBy;
    private LocalDateTime reservedAt;

}
