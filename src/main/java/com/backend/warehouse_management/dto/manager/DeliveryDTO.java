package com.backend.warehouse_management.dto.manager;

import com.backend.warehouse_management.dto.client.OrderDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DeliveryDTO {

    private Long deliveryId;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate scheduledDate;
    private List<OrderDTO> orders;
    private Long truckId;
    private Double remainingSpace;
}
