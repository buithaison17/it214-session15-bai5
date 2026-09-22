package org.example.orchestratorservice.client;

import org.example.orchestratorservice.dto.ReservationRequest;
import org.example.orchestratorservice.dto.ReservationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "concert-service")
public interface ConcertClient {
    @PostMapping("/concerts/reserve")
    ReservationResponse reserveSeats(@RequestBody ReservationRequest request);

    @PostMapping("/concerts/release/{bookingId}")
    ReservationResponse releaseSeats(@PathVariable String bookingId);
}
