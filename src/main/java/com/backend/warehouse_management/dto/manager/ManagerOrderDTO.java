package com.backend.warehouse_management.dto.manager;

import com.backend.warehouse_management.dto.client.OrderItemDTO;
import com.backend.warehouse_management.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagerOrderDTO {

    private Long id;
    private UUID orderNumber;
    private LocalDate submittedDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private Double TotalPrice;
    private List<OrderItemDTO> orderItems;

}
