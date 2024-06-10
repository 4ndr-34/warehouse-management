package com.backend.warehouse_management.mapper;

import com.backend.warehouse_management.dto.client.OrderItemDTO;
import com.backend.warehouse_management.entity.OrderItem;
import org.mapstruct.Mapper;

@Mapper
public interface OrderItemMapper {

    OrderItemDTO orderItemToOrderItemDTO(OrderItem orderItem);

    OrderItem orderItemDTOToOrderItem(OrderItemDTO orderItemDTO);
}
