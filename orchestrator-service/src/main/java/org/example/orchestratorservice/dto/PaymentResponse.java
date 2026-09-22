package org.example.orchestratorservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.orchestratorservice.constants.PaymentStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String bookingId;
    private PaymentStatus status;
    private double amount;
    private String message;
}
