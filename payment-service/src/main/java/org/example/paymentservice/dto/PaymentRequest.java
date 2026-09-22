package org.example.paymentservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    private String bookingId;
    private String customerId;
    private String customerEmail;
    private double amount;
}
