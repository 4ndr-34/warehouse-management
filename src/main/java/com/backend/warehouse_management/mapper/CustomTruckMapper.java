package com.backend.warehouse_management.mapper;

import com.backend.warehouse_management.dto.manager.TruckDTO;
import com.backend.warehouse_management.entity.Truck;

public class CustomTruckMapper {

    public static Truck managerMapTruckDTOToTruck(TruckDTO truckDTO) {
        Truck truck = new Truck();
        truck.setChassisNumber(truckDTO.getChassisNumber());
        truck.setLicensePlate(truckDTO.getLicensePlate());
        truck.setCapacity(truckDTO.getCapacity());
        //TODO - deliveries for this truck
        return truck;
    }

    public static TruckDTO managerMapTruckToTruckDTO(Truck truck) {
        TruckDTO truckDTO = new TruckDTO();
        truckDTO.setId(truck.getId());
        truckDTO.setChassisNumber(truck.getChassisNumber());
        truckDTO.setLicensePlate(truck.getLicensePlate());
        truckDTO.setCapacity(truck.getCapacity());
        //TODO - deliveries for this truck,
        //will be implemented when completing delivery functionalities
        return truckDTO;
    }

    public static Truck managerEditTruckDetailsFromDTO(Truck truck, TruckDTO truckDTO) {
        if(truckDTO.getChassisNumber() != null) {
            truck.setChassisNumber(truckDTO.getChassisNumber());
        }
        if(truckDTO.getLicensePlate() != null) {
            truck.setLicensePlate(truckDTO.getLicensePlate());
        }
        if(truckDTO.getCapacity() != null) {
            truck.setCapacity(truck.getCapacity());
        }
        //TODO - deliveries for this truck
        return truck;
    }

}
