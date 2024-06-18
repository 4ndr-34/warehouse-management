package com.backend.warehouse_management.dto.client;

import lombok.Data;

@Data
public class UpdateOrderItemRequest {

    Long orderId;
    Long orderItemId;
    int quantity;

}
