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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final OrderRepository orderRepository;

    @Override
    public List<OrderDTO> managerGetAllOrders() {
        //retrieve orders in descending order to re-order them in the list
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "submittedDate"));
        List<OrderDTO> orderToReturn = new ArrayList<>();
        //insert items in the dto list
        for(Order order : orders) {
            //map order to the general details
            orderToReturn.add(CustomOrderMapper
                    .managerMapOrderToOrderDTOGeneralDetails(order));
        }
        //return the list
        return orderToReturn;
    }

    @Override
    public OrderDTO managerGetDetailedOrder(Long orderId) throws Exception {
        //retrieve order
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        //check if it exists
        if(optionalOrder.isPresent()){
            //return order with all details
            return CustomOrderMapper.managerMapOrderToOrderDTODetailed(optionalOrder.get());
        }
        //if order doesn't exist, throw exception
        else {
            throw new Exception("Order with ID: " + orderId + ", does not exist");
        }
    }

}
