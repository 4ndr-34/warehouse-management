package com.backend.warehouse_management.mapper;

import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.entity.Order;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {

    Order orderDTOToOrder(OrderDTO orderDTO);

    OrderDTO orderToOrderDTO(Order order);

}
