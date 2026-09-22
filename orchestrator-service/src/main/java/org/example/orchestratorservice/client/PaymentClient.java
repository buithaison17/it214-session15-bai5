package org.example.orchestratorservice.client;

import org.example.orchestratorservice.dto.PaymentRequest;
import org.example.orchestratorservice.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "payment-service"
)
public interface PaymentClient {
    @PostMapping("/payments/process")
    PaymentResponse processPayment(
            @RequestBody PaymentRequest request
    );

    @PostMapping("/payments/refund/{bookingId}")
    PaymentResponse refundPayment(
            @PathVariable String bookingId
    );
}
