package org.example.paymentservice.dto;

import lombok.*;
import org.example.paymentservice.constants.PaymentStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String bookingId;
    private PaymentStatus status;
    private double amount;
    private String message;
}
