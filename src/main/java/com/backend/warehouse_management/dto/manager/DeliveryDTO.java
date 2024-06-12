package com.backend.warehouse_management.dto.manager;

import com.backend.warehouse_management.dto.client.OrderDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTO {

    private Long deliveryId;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate scheduledDate;
    private List<OrderDTO> orders;
    private Long truckId;
    private Double remainingSpace;
}
