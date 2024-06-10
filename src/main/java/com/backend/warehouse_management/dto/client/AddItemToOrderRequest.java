package com.backend.warehouse_management.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddItemToOrderRequest {

    private Long productId;
    private Integer quantity;
    private Long orderId;

}
