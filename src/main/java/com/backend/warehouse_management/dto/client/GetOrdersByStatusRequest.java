package com.backend.warehouse_management.dto.client;

import com.backend.warehouse_management.enums.OrderStatus;
import lombok.Data;

@Data
public class GetOrdersByStatusRequest {

    private Long userId;
    private OrderStatus status;

}
