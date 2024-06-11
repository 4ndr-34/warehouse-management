package com.backend.warehouse_management.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TruckDTO {

    private Long id;
    private String chassisNumber;
    private String licensePlate;
    private Double capacity;
}
