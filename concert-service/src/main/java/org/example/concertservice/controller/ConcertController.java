package org.example.concertservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.concertservice.dto.BookingRequest;
import org.example.concertservice.dto.ReservationResponse;
import org.example.concertservice.entity.Seat;
import org.example.concertservice.service.SeatReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/concerts")
@RequiredArgsConstructor
public class ConcertController {
    private final SeatReservationService seatReservationService;

    @PostMapping("/reserve")
    public ReservationResponse reserveSeats(
            @RequestBody BookingRequest request
    ) {

        return seatReservationService.reserveSeats(request);
    }

    @PostMapping("/confirm/{bookingId}")
    public ReservationResponse confirmReservation(
            @PathVariable String bookingId
    ) {

        return seatReservationService.confirmReservation(
                bookingId
        );
    }

    @PostMapping("/release/{bookingId}")
    public ReservationResponse releaseSeats(
            @PathVariable String bookingId
    ) {
        return seatReservationService.releaseSeats(
                bookingId
        );
    }

    @GetMapping("/{concertCode}/seats")
    public List<Seat> getSeats(
            @PathVariable String concertCode
    ) {
        return seatReservationService.getSeats(
                concertCode
        );
    }
}