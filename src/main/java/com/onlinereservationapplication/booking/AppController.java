package com.onlinereservationapplication.booking;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/v1")
public class AppController
{
    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;

    AppController(BookingRepository bookingRepository,
                  PassengerRepository passengerRepository)
    {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
    }

    @GetMapping("fetch/all/bookings")
    public ResponseEntity<List<Booking>> fetchAllBookings()
    {
        return ResponseEntity.ok(bookingRepository.findAll());
    }

    @GetMapping("fetch/all/passengers")
    public ResponseEntity<List<Passenger>> fetchAllPassengers()
    {
        return ResponseEntity.ok(passengerRepository.findAll());
    }

}
