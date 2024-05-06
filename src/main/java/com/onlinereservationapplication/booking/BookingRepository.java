package com.onlinereservationapplication.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String>
{
    // You can add custom query methods here if needed
}

