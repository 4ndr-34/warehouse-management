package com.backend.warehouse_management.state;

import com.backend.warehouse_management.dto.client.OrderDTO;

public interface OrderState<Order> {

    OrderDTO processOrder(Order order) throws Exception;

    OrderDTO createOrder(Long userId, Order order);
}
