package com.backend.warehouse_management.repository;

import com.backend.warehouse_management.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    //List<Delivery> findDeliveriesByScheduledDateBetween(LocalDate today, LocalDate limit);

    boolean existsByScheduledDateAndTruckId(LocalDate scheduledDate, Long truckId);
}
