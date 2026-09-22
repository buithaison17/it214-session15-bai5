package org.example.concertservice.service;

import org.example.concertservice.constants.SeatStatus;
import org.example.concertservice.dto.BookingRequest;
import org.example.concertservice.dto.ReservationResponse;
import org.example.concertservice.entity.Seat;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class SeatReservationService {
    private final Map<String, Seat> seats =
            new ConcurrentHashMap<>();

    public SeatReservationService() {
        // Khởi tạo 100 ghế cho concert
        initializeSeats("LIVE-HCM-2026-ULTRA");
    }

    private void initializeSeats(String concertCode) {
        for (int i = 1; i <= 100; i++) {
            String seatNumber = String.format("A%02d", i);
            Seat seat = Seat.builder()
                    .seatNumber(seatNumber)
                    .concertCode(concertCode)
                    .status(SeatStatus.AVAILABLE)
                    .build();
            seats.put(
                    createKey(concertCode, seatNumber),
                    seat
            );
        }
    }

    public synchronized ReservationResponse reserveSeats(
            BookingRequest request
    ) {
        String bookingId = request.getBookingId();
        String concertCode = request.getConcertCode();
        int quantity = request.getTicketQuantity();
        System.out.println(
                "[ConcertService] Reserving "
                        + quantity
                        + " seats for booking "
                        + bookingId
        );
        if (quantity <= 0) {
            return new ReservationResponse(
                    false,
                    bookingId,
                    "Ticket quantity must be greater than 0",
                    List.of()
            );
        }

        List<Seat> availableSeats = seats.values()
                .stream()
                .filter(seat ->
                        seat.getConcertCode().equals(concertCode)
                                && seat.getStatus() == SeatStatus.AVAILABLE
                )
                .limit(quantity)
                .toList();

        if (availableSeats.size() < quantity) {

            System.out.println(
                    "[ConcertService] ERROR: Not enough available seats!"
            );

            return new ReservationResponse(
                    false,
                    bookingId,
                    "Not enough available seats",
                    List.of()
            );
        }

        List<String> reservedSeatNumbers =
                new ArrayList<>();
        for (Seat seat : availableSeats) {
            seat.setStatus(SeatStatus.RESERVED);
            seat.setReservedBy(bookingId);
            seat.setReservedAt(
                    LocalDateTime.now()
            );
            reservedSeatNumbers.add(
                    seat.getSeatNumber()
            );
            System.out.println(
                    "[ConcertService] Seat "
                            + seat.getSeatNumber()
                            + ": AVAILABLE -> RESERVED"
            );
        }

        System.out.println(
                "[ConcertService] Booking "
                        + bookingId
                        + " reserved seats: "
                        + reservedSeatNumbers
        );

        return new ReservationResponse(
                true,
                bookingId,
                "Seats reserved successfully",
                reservedSeatNumbers
        );
    }

    public synchronized ReservationResponse confirmReservation(
            String bookingId
    ) {

        List<Seat> reservedSeats = seats.values()
                .stream()
                .filter(seat ->
                        bookingId.equals(seat.getReservedBy())
                                && seat.getStatus()
                                == SeatStatus.RESERVED
                )
                .toList();

        if (reservedSeats.isEmpty()) {

            return new ReservationResponse(
                    false,
                    bookingId,
                    "No reserved seats found",
                    List.of()
            );
        }

        List<String> seatNumbers =
                new ArrayList<>();

        for (Seat seat : reservedSeats) {
            seat.setStatus(SeatStatus.BOOKED);
            seatNumbers.add(
                    seat.getSeatNumber()
            );
            System.out.println(
                    "[ConcertService] Seat "
                            + seat.getSeatNumber()
                            + ": RESERVED -> BOOKED"
            );
        }

        return new ReservationResponse(
                true,
                bookingId,
                "Booking confirmed",
                seatNumbers
        );
    }

    public synchronized ReservationResponse releaseSeats(
            String bookingId
    ) {

        List<Seat> reservedSeats = seats.values()
                .stream()
                .filter(seat ->
                        bookingId.equals(seat.getReservedBy())
                                && seat.getStatus()
                                == SeatStatus.RESERVED
                )
                .toList();

        List<String> releasedSeats =
                new ArrayList<>();

        for (Seat seat : reservedSeats) {
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setReservedBy(null);
            seat.setReservedAt(null);
            releasedSeats.add(
                    seat.getSeatNumber()
            );

            System.out.println(
                    "[ConcertService] Seat "
                            + seat.getSeatNumber()
                            + ": RESERVED -> AVAILABLE"
            );
        }

        return new ReservationResponse(
                true,
                bookingId,
                "Seats released successfully",
                releasedSeats
        );
    }

    public List<Seat> getSeats(
            String concertCode
    ) {
        return seats.values()
                .stream()
                .filter(seat ->
                        seat.getConcertCode()
                                .equals(concertCode)
                )
                .toList();
    }

    private String createKey(
            String concertCode,
            String seatNumber
    ) {
        return concertCode + "-" + seatNumber;
    }
}
