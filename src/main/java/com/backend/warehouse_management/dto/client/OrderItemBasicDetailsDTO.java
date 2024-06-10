package com.backend.warehouse_management.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//to be printed when customer receives order details
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemBasicDetailsDTO {

    private String itemName;
    private Integer quantity;
    private Double itemPrice;

}
