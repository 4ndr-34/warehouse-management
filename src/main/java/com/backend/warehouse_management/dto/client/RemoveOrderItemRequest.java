package com.backend.warehouse_management.dto.client;

import lombok.Data;

@Data
public class RemoveOrderItemRequest {

    Long orderItemId;
    Long orderId;

}
