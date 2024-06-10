package com.backend.warehouse_management.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {

    private String itemName;
    private Integer quantity;
    private Double price;
    private Long productId;
    private Long orderId;


}
