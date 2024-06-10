package com.backend.warehouse_management.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private String itemName;
    private Integer quantity;
    private Double price;
    private Double volume;

}
