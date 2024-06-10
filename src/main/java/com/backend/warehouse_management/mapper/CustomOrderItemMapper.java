package com.backend.warehouse_management.mapper;

import com.backend.warehouse_management.dto.client.OrderItemBasicDetailsDTO;
import com.backend.warehouse_management.entity.OrderItem;

public class CustomOrderItemMapper {

    public static OrderItemBasicDetailsDTO getBasicDetailsFromOrderItem(OrderItem orderItem) {
        OrderItemBasicDetailsDTO orderItemBasicDetailsDTO = new OrderItemBasicDetailsDTO();
        orderItemBasicDetailsDTO.setItemName(orderItem.getItemName());
        orderItemBasicDetailsDTO.setItemPrice(orderItem.getPrice());
        orderItemBasicDetailsDTO.setQuantity(orderItem.getQuantity());
        return orderItemBasicDetailsDTO;
    }


}
