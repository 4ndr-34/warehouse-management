package com.backend.warehouse_management.dto.manager;

import com.backend.warehouse_management.dto.client.OrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTO {

    private LocalDate scheduledDate;
    private List<OrderDTO> orders;
    private TruckDTO truck;
    private Double remainingSpace;
}
