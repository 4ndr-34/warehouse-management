package com.backend.warehouse_management.dto.manager;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDeliveryRequest {

    private Long truckId;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate scheduledDate;
}
