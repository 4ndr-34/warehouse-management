package com.backend.warehouse_management.dto.manager;

import lombok.Data;

@Data
public class AddOrderToDeliveryRequest {
    private Long deliveryId;
    private Long orderId;
}
