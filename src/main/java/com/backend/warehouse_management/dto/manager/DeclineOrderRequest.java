package com.backend.warehouse_management.dto.manager;

import lombok.Data;

@Data
public class DeclineOrderRequest {
    private Long orderId;
    private String reason;
}
