package com.backend.warehouse_management.dto.manager;

import lombok.Data;

@Data
public class OrderAndDeliveryRequest {
    private Long deliveryId;
    private Long orderId;
}
