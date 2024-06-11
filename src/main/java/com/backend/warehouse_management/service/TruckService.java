package com.backend.warehouse_management.service;

import com.backend.warehouse_management.dto.manager.TruckDTO;

public interface TruckService {

    TruckDTO addTruck(TruckDTO truckDTO) throws Exception;

    TruckDTO editTruckDetails(Long truckId, TruckDTO truckDTO) throws Exception;

    TruckDTO getTruckByLicensePlate(String licensePlate) throws Exception;

    void removeTruckByLicensePlate(String licensePlate) throws Exception;

}
