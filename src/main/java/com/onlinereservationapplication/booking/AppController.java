package com.onlinereservationapplication.booking;


import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;



@RestController
@RequestMapping("api/v1")
public class AppController
{
    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    WebClient.Builder webClientBuilder;

    AppController(BookingRepository bookingRepository,
                  PassengerRepository passengerRepository,
                  WebClient.Builder webClientBuilder)
    {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.webClientBuilder = webClientBuilder;
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

    @PostMapping("request/for/booking")
    public ResponseEntity <String> requestForBooking(@RequestBody BookingRequest bookingRequest)
    {
        // Fetch if the requested seats available on inventory for the requested busNumber
        // URI get/seatsAvailable/{busNumber}
        String URL = "http://localhost:8088/api/v1/get/seatsAvailable/"
                + bookingRequest.getBusNumber();

        Integer getAvailableSeats = webClientBuilder.build()
                .get()
                .uri(URL)
                .retrieve()
                .bodyToMono(Integer.class)
                .block(); //SYNCHRONOUS REQUEST

        if (getAvailableSeats >= bookingRequest.getRequiredSeats())
        {
            Booking booking = new Booking();

            // get the source, destination from Busroute model
            String busRouteURL = "http://localhost:8086/api/v1/fetch/busRoute/"
                    + bookingRequest.getBusNumber();

            Busroute busRoute = webClientBuilder.build()
                    .get()
                    .uri(busRouteURL)
                    .retrieve()
                    .bodyToMono(Busroute.class)
                    .block(); //SYNCHRONOUS REQUEST

            booking.setBookingNumber(String.valueOf(UUID.randomUUID()));
            booking.setBusNumber(bookingRequest.getBusNumber());
            booking.setBookingDate(LocalDate.now());
            booking.setSource(busRoute.getSource());
            booking.setDestination(busRoute.getDestination());
            booking.setNumberOfSeats(bookingRequest.getRequiredSeats());
            booking.setStatus("pending");

            bookingRepository.save(booking);

            // Request Payment table to create an entry
            String paymentURL = "http://localhost:8087/api/v1/add/payment/"
                    + booking.getBookingNumber();

            String resAddPayment = webClientBuilder.build()
                    .post()
                    .uri(paymentURL)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); //SYNCHRONOUS REQUEST

            // Payment Service requests Inventory Service to reduce the available seats on inventory table

            String InventoryURL = "http://localhost:8088/api/v1/update/busInventory/"
                    + bookingRequest.getBusNumber()
                    + "/"
                    + bookingRequest.getRequiredSeats();

            String resUpdateBookedSeats = webClientBuilder.build()
                    .post()
                    .uri(InventoryURL)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); //SYNCHRONOUS REQUEST

            // Inventory Service then call back to Booking Service and Update Booking as confirmed.
            booking.setStatus("confirmed");
            bookingRepository.save(booking);

            // Finally, create passenger table with booking number and passenger ID
            Passenger passenger = new Passenger();
            passenger.setBookingNumber(booking.getBookingNumber());
            passenger.setPassengerid(String.valueOf(UUID.randomUUID()));
            passengerRepository.save(passenger);

            return ResponseEntity.ok("Booking confirmed successfully");

        }
        else
        {
            return ResponseEntity.ok("Not sufficient seats on this bus Number; try some other bus Number");
        }
    }
}
