package org.example.paymentservice.service;

import org.example.paymentservice.constants.PaymentStatus;
import org.example.paymentservice.dto.PaymentRequest;
import org.example.paymentservice.dto.PaymentResponse;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentProcessingService {
    private final Map<String, PaymentResponse> payments =
            new ConcurrentHashMap<>();

    /**
     * Xử lý thanh toán
     */
    public PaymentResponse processPayment(PaymentRequest request) {
        System.out.println(
                "[PaymentService] Processing payment for booking "
                        + request.getBookingId()
        );

        System.out.println(
                "[PaymentService] Amount: "
                        + request.getAmount()
                        + " VND"
        );

        // Nếu booking đã thanh toán thành công
        PaymentResponse existingPayment =
                payments.get(request.getBookingId());

        if (existingPayment != null
                && existingPayment.getStatus() == PaymentStatus.SUCCESS) {
            System.out.println(
                    "[PaymentService] Payment already completed for booking "
                            + request.getBookingId()
            );
            return existingPayment;
        }

        PaymentResponse response = new PaymentResponse(
                request.getBookingId(),
                PaymentStatus.SUCCESS,
                request.getAmount(),
                "Payment completed successfully"
        );

        payments.put(request.getBookingId(), response);

        System.out.println(
                "[PaymentService] Payment SUCCESS for booking "
                        + request.getBookingId()
        );

        return response;
    }

    public PaymentResponse refundPayment(String bookingId) {

        PaymentResponse payment = payments.get(bookingId);

        if (payment == null) {

            System.out.println(
                    "[PaymentService] No payment found for booking "
                            + bookingId
            );

            return new PaymentResponse(
                    bookingId,
                    PaymentStatus.FAILED,
                    0,
                    "Payment not found"
            );
        }

        if (payment.getStatus() == PaymentStatus.REFUNDED) {

            System.out.println(
                    "[PaymentService] Payment already refunded for booking "
                            + bookingId
            );

            return payment;
        }

        if (payment.getStatus() != PaymentStatus.SUCCESS) {

            System.out.println(
                    "[PaymentService] Cannot refund unsuccessful payment "
                            + bookingId
            );

            return new PaymentResponse(
                    bookingId,
                    PaymentStatus.FAILED,
                    payment.getAmount(),
                    "Payment was not successful"
            );
        }

        PaymentResponse refund = new PaymentResponse(
                bookingId,
                PaymentStatus.REFUNDED,
                payment.getAmount(),
                "Payment refunded successfully"
        );

        payments.put(bookingId, refund);

        System.out.println(
                "[PaymentService] REFUND "
                        + payment.getAmount()
                        + " VND for booking "
                        + bookingId
        );

        return refund;
    }
}
