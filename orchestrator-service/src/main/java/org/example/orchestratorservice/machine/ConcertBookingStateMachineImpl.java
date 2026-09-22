package org.example.orchestratorservice.machine;

import lombok.RequiredArgsConstructor;
import org.example.orchestratorservice.client.ConcertClient;
import org.example.orchestratorservice.client.PaymentClient;
import org.example.orchestratorservice.constants.PaymentStatus;
import org.example.orchestratorservice.dto.*;
import org.example.orchestratorservice.entity.BookingTransaction;
import org.example.orchestratorservice.event.BookingEvent;
import org.example.orchestratorservice.state.BookingState;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertBookingStateMachineImpl implements ConcertBookingStateMachine {
    private final PaymentClient paymentClient;
    private final ConcertClient concertClient;
    private static final int MAX_PAYMENT_ATTEMPTS = 3;
    private static final long RETRY_DELAY = 2000;

    @Override
    public BookingTransaction process(BookingRequest request) {
        BookingTransaction transaction =
                new BookingTransaction(
                        request.getBookingId(),
                        BookingState.INITIATED,
                        false,
                        false,
                        "Booking started"
                );

        transition(
                transaction,
                BookingEvent.PROCESS_PAYMENT,
                BookingState.PAYMENT_PENDING
        );

        boolean paymentSuccess = processPaymentWithRetry(
                request,
                transaction
        );

        if (!paymentSuccess) {
            transition(
                    transaction,
                    BookingEvent.PAYMENT_FAILED,
                    BookingState.CANCELLED
            );

            transaction.setMessage(
                    "Payment failed after "
                            + MAX_PAYMENT_ATTEMPTS
                            + " attempts"
            );

            printFinalState(transaction);

            return transaction;
        }

        transition(
                transaction,
                BookingEvent.PAYMENT_SUCCESS,
                BookingState.PAYMENT_COMPLETED
        );

        transaction.setPaymentCompleted(true);

        transition(
                transaction,
                BookingEvent.RESERVE_SEATS,
                BookingState.SEAT_RESERVING
        );

        try {

            ReservationRequest reservationRequest =
                    new ReservationRequest(
                            request.getBookingId(),
                            request.getConcertCode(),
                            request.getTicketQuantity()
                    );

            ReservationResponse reservationResponse =
                    concertClient.reserveSeats(reservationRequest);

            if (reservationResponse == null
                    || !reservationResponse.isSuccess()) {

                return handleReservationFailure(
                        request,
                        transaction,
                        reservationResponse != null
                                ? reservationResponse.getMessage()
                                : "Reservation failed"
                );
            }

            transaction.setSeatsReserved(true);

            transition(
                    transaction,
                    BookingEvent.RESERVATION_SUCCESS,
                    BookingState.BOOKING_CONFIRMED
            );

            transaction.setMessage(
                    "Booking confirmed successfully"
            );

            printFinalState(transaction);

            return transaction;

        } catch (Exception e) {

            return handleReservationFailure(
                    request,
                    transaction,
                    e.getMessage()
            );
        }
    }

    private boolean processPaymentWithRetry(
            BookingRequest request,
            BookingTransaction transaction
    ) {

        for (int attempt = 1;
             attempt <= MAX_PAYMENT_ATTEMPTS;
             attempt++) {

            System.out.println(
                    "[Orchestrator] RetryPolicy: Activity 'processPayment' "
                            + "- Attempt "
                            + attempt
                            + "/"
                            + MAX_PAYMENT_ATTEMPTS
            );

            try {

                PaymentRequest paymentRequest =
                        new PaymentRequest(
                                request.getBookingId(),
                                request.getCustomerId(),
                                request.getCustomerEmail(),
                                request.getAmount()
                        );

                PaymentResponse response =
                        paymentClient.processPayment(paymentRequest);

                if (response != null
                        && response.getStatus()
                        == PaymentStatus.SUCCESS) {

                    return true;
                }

                System.out.println(
                        "[Orchestrator] Payment failed on attempt "
                                + attempt
                );

            } catch (Exception e) {

                System.out.println(
                        "[Orchestrator] Payment error: "
                                + e.getMessage()
                );
            }

            if (attempt < MAX_PAYMENT_ATTEMPTS) {

                try {
                    Thread.sleep(RETRY_DELAY);
                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();

                    return false;
                }
            }
        }

        return false;
    }

    private BookingTransaction handleReservationFailure(
            BookingRequest request,
            BookingTransaction transaction,
            String reason
    ) {

        System.out.println(
                "[Orchestrator] Reservation failed: "
                        + reason
        );

        transition(
                transaction,
                BookingEvent.RESERVATION_FAILED,
                BookingState.CANCELLED
        );

        if (transaction.isPaymentCompleted()) {
            System.out.println(
                    "[Orchestrator] Compensation: "
                            + "Refunding payment for booking "
                            + request.getBookingId()
            );

            try {

                PaymentResponse refundResponse =
                        paymentClient.refundPayment(
                                request.getBookingId()
                        );

                if (refundResponse != null) {

                    System.out.println(
                            "[Orchestrator] Refund status: "
                                    + refundResponse.getStatus()
                    );
                }

            } catch (Exception e) {

                System.out.println(
                        "[Orchestrator] Refund failed: "
                                + e.getMessage()
                );
            }
        }

        if (transaction.isSeatsReserved()) {

            try {

                concertClient.releaseSeats(
                        request.getBookingId()
                );

                System.out.println(
                        "[Orchestrator] Seats released for booking "
                                + request.getBookingId()
                );

            } catch (Exception e) {

                System.out.println(
                        "[Orchestrator] Seat release failed: "
                                + e.getMessage()
                );
            }
        }

        transaction.setMessage(
                "Booking cancelled. Compensation executed."
        );

        printFinalState(transaction);

        return transaction;
    }

    private void transition(
            BookingTransaction transaction,
            BookingEvent event,
            BookingState newState
    ) {

        BookingState oldState =
                transaction.getCurrentState();

        transaction.setCurrentState(newState);

        System.out.println(
                "[Orchestrator] State: "
                        + oldState
                        + " -> Event: "
                        + event
                        + " -> New State: "
                        + newState
        );
    }

    private void printFinalState(
            BookingTransaction transaction
    ) {

        System.out.println(
                "[Orchestrator] Final State: "
                        + transaction.getCurrentState()
                        + " for booking "
                        + transaction.getBookingId()
        );
    }
}
