package com.backend.warehouse_management.service.impl;

import com.backend.warehouse_management.dto.manager.TruckDTO;
import com.backend.warehouse_management.entity.Truck;
import com.backend.warehouse_management.mapper.CustomTruckMapper;
import com.backend.warehouse_management.repository.TruckRepository;
import com.backend.warehouse_management.service.TruckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TruckServiceImpl implements TruckService {

    private final TruckRepository truckRepository;

    @Override
    public TruckDTO addTruck(TruckDTO truckDTO) throws Exception {
        //if truck with this license plate is not present in database, then add the new truck
        if(!truckRepository.existsByLicensePlate(truckDTO.getLicensePlate())){
            //return the dto after saving the truck object
            return CustomTruckMapper.managerMapTruckToTruckDTO(
                    //save the truck object after mapping the dto
                    truckRepository.save(CustomTruckMapper
                            .managerMapTruckDTOToTruck(truckDTO))
            );
        }
        //if truck is present in the database, throw exception
        else {
            throw new Exception("A truck with this license plate already exists.");
        }
    }

    @Override
    public TruckDTO editTruckDetails(Long truckId, TruckDTO truckDTO) throws Exception {
        //retrieve truck
        Optional<Truck> optionalTruck = truckRepository.findById(truckId);
        //check if truck exists
        if(optionalTruck.isPresent()) {
            //save truck details after mapping the dto's accordingly
            return CustomTruckMapper.managerMapTruckToTruckDTO(
                    //edit the truck details
                    truckRepository.save(CustomTruckMapper.managerEditTruckDetailsFromDTO(optionalTruck.get(), truckDTO)));
        }
        //if truck doesn't exist, throw exception
        else {
            throw new Exception("The truck with the ID: " + truckId + " is not present in the database");
        }
    }

    @Override
    public TruckDTO getTruckByLicensePlate(String licensePlate) throws Exception {
        //check if truck exists
        if(truckRepository.existsByLicensePlate(licensePlate)) {
            return CustomTruckMapper.managerMapTruckToTruckDTO(
                    truckRepository.findByLicensePlate(licensePlate).get());
        }
        //if truck doesn't exist, throw exception
        else {
            throw new Exception("The truck with the license plate: " +licensePlate + "is not present in the database");
        }
    }

    @Override
    public void removeTruckByLicensePlate(String licensePlate) throws Exception {
        //check if truck exists
        if(truckRepository.existsByLicensePlate(licensePlate)){
            //remove truck from table
            truckRepository.delete(truckRepository.findByLicensePlate(licensePlate).get());
        }
        //if truck doesn't exist, throw exception
        else {
            throw new Exception("The truck with the license plate: " +licensePlate + "is not present in the database");
        }
    }
}
