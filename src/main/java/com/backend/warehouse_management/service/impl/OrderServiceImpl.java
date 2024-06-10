package com.backend.warehouse_management.service.impl;

import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.dto.client.OrderItemDTO;
import com.backend.warehouse_management.entity.Order;
import com.backend.warehouse_management.enums.OrderStatus;
import com.backend.warehouse_management.mapper.CustomOrderMapper;
import com.backend.warehouse_management.repository.OrderRepository;
import com.backend.warehouse_management.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;
    @Override
    public OrderDTO createNewOrder(Long id, OrderDTO orderDTO) {
        return null;
    }

    @Override
    public OrderDTO addItemToOrder(Long userId, OrderItemDTO orderItemDTO) {
        return null;
    }

    @Override
    public List<OrderDTO> getAllOrdersForClientId(Long userId) {
        List<Order> ordersForUser = orderRepository.findAllByUserId(userId);
        List<OrderDTO> orderListForReturn = new ArrayList<>();
        for(Order order : ordersForUser) {
            orderListForReturn.add(CustomOrderMapper.clientMapOrderToOrderDTO(order));
        }
        return orderListForReturn;
    }

    @Override
    public List<OrderDTO> getOrdersByStatus(Long userId, OrderStatus orderStatus) {
        List<Order> ordersOfStatus = orderRepository.findAllByUserIdAndOrderStatus(userId, orderStatus);
        List<OrderDTO> orderListForReturn = new ArrayList<>();
        for(Order order : ordersOfStatus) {
            orderListForReturn.add(CustomOrderMapper.clientMapOrderToOrderDTO(order));
        }
        return orderListForReturn;
    }
}
