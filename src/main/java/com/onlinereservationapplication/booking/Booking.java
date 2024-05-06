package com.onlinereservationapplication.booking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @Column(name = "booking_number", nullable = false, length = 20)
    private String bookingNumber;

    @Column(name = "bus_number", nullable = false, length = 10)
    private String busNumber;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "source", nullable = false, length = 50)
    private String source;

    @Column(name = "destination", nullable = false, length = 50)
    private String destination;

    @Column(name = "number_of_seats", nullable = false)
    private Integer numberOfSeats;

    @ColumnDefault("pending")
    @Column(name = "status", length = 20)
    private String status;

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}