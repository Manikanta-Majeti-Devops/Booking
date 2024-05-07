package com.onlinereservationapplication.booking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BusrouteRepository extends JpaRepository<Busroute, String>
{
    Busroute findByBusNumber(String busNumber);
}
