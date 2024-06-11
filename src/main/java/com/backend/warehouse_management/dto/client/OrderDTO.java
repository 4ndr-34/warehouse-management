package com.backend.warehouse_management.dto.client;

import com.backend.warehouse_management.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {

    private UUID orderNumber;
    private LocalDate submittedDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private Long userId;
    private List<OrderItemBasicDetailsDTO> orderItems;
    private Double totalPrice;
    private LocalDate deadline;
    private Long deliveryId;
    private String decliningReason;
}
