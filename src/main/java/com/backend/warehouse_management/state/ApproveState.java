package com.backend.warehouse_management.state;

import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.entity.Order;
import com.backend.warehouse_management.exception.OrderCannotBeProcessedException;

public class ApproveState implements OrderState<Order>{
    @Override
    public OrderDTO processOrder(Order order) throws Exception {

        return null;
    }

    @Override
    public OrderDTO createOrder(Long userId, Order order) {
        throw new OrderCannotBeProcessedException();
    }
}
