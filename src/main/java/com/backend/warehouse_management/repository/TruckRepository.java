package com.backend.warehouse_management.repository;

import com.backend.warehouse_management.entity.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TruckRepository extends JpaRepository<Truck, Long> {

    @Query("select truck from Truck truck where truck.licensePlate = :licensePlate")
    Optional<Truck> findByLicensePlate(String licensePlate);

    boolean existsByLicensePlate(String licensePlate);

}
