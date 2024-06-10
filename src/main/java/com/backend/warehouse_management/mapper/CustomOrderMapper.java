package com.backend.warehouse_management.mapper;

import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.entity.Order;

import java.util.stream.Collectors;

public class CustomOrderMapper {


    public static OrderDTO clientMapOrderToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setOrderNumber(order.getOrderNumber());
        orderDTO.setOrderStatus(order.getOrderStatus());
        orderDTO.setUserId(order.getUser().getId());
        if(order.getDelivery() != null){
            orderDTO.setDeliveryId(order.getDelivery().getId());
        }

        orderDTO.setDeadline(order.getDeadline());

        if(order.getOrderItems() != null){
            orderDTO.setOrderItems(order.getOrderItems()
                    .stream()
                    .map(CustomOrderItemMapper::getBasicDetailsFromOrderItem)
                    .collect(Collectors.toList()));
        }
        orderDTO.setTotalPrice(order.getTotalPrice());
        return orderDTO;
    }

}
