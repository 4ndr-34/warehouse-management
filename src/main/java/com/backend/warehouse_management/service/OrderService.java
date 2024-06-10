package com.backend.warehouse_management.service;

import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.dto.client.OrderItemDTO;
import com.backend.warehouse_management.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderDTO createNewOrder(Long id, OrderDTO orderDTO);

    OrderDTO addItemToOrder(Long userId, OrderItemDTO orderItemDTO);

    List<OrderDTO> getAllOrdersForClientId(Long userId);

    List<OrderDTO> getOrdersByStatus(Long userId, OrderStatus orderStatus);
}
