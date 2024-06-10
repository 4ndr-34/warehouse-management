package com.backend.warehouse_management.service.impl;

import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.entity.Order;
import com.backend.warehouse_management.mapper.CustomOrderMapper;
import com.backend.warehouse_management.repository.OrderRepository;
import com.backend.warehouse_management.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final OrderRepository orderRepository;

    @Override
    public List<OrderDTO> managerGetAllOrders() {
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "submittedDate"));
        List<OrderDTO> orderToReturn = new ArrayList<>();
        for(Order order : orders) {
            orderToReturn.add(CustomOrderMapper
                    .managerMapOrderToOrderDTOGeneralDetails(order));
        }
        return orderToReturn;
    }

}
