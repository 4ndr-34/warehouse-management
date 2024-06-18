package com.backend.warehouse_management.state;

import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.entity.Order;
import com.backend.warehouse_management.enums.OrderStatus;
import com.backend.warehouse_management.exception.OrderCannotBeProcessedException;
import com.backend.warehouse_management.mapper.CustomOrderMapper;
import com.backend.warehouse_management.repository.OrderRepository;
import com.backend.warehouse_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateState implements OrderState<Order>{

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    @Override
    public OrderDTO processOrder(Order order) throws Exception {
        throw new OrderCannotBeProcessedException();
    }

    @Override
    public OrderDTO createOrder(Long userId, Order order) {
        order.setOrderStatus(OrderStatus.CREATED);
        order.setOrderNumber(UUID.randomUUID());
        order.setTotalPrice(0.0);
        order.setSubmittedDate(LocalDate.now());
        order.setUser(userRepository.findById(userId).get());
        order.setOrderStatus(OrderStatus.CREATED);
        return CustomOrderMapper.basicMapOrderToOrderDTO(orderRepository.save(order));
    }
}
