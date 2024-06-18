package com.backend.warehouse_management.state;

import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.entity.Order;
import com.backend.warehouse_management.enums.OrderStatus;
import com.backend.warehouse_management.mapper.CustomOrderMapper;
import com.backend.warehouse_management.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AwaitingApprovalState implements OrderState<Order>{

    private final OrderRepository orderRepository;

    @Override
    public OrderDTO processOrder(Order order) {
        order.setOrderStatus(OrderStatus.AWAITING_APPROVAL);
        return CustomOrderMapper.basicMapOrderToOrderDTO(orderRepository.save(order));
    }

    @Override
    public OrderDTO createOrder(Long userId, Order order) {
        return null;
    }
}
