package com.backend.warehouse_management.service.impl;

import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.entity.Order;
import com.backend.warehouse_management.enums.OrderStatus;
import com.backend.warehouse_management.mapper.CustomOrderMapper;
import com.backend.warehouse_management.repository.OrderRepository;
import com.backend.warehouse_management.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final OrderRepository orderRepository;

    @Override
    public List<OrderDTO> managerGetAllOrders() {
        //retrieve orders in descending order to re-order them in the return list
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "submittedDate"));
        //map the orders to a new list and return them
        return orders.stream()
                .map(CustomOrderMapper::managerMapOrderToOrderDTOGeneralDetails)
                .collect(Collectors.toList());
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

    @Override
    public OrderDTO managerApproveOrder(Long orderId) throws Exception {
        //retrieve order
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        //check if order exists
        if(optionalOrder.isPresent()) {
            //change order status
            optionalOrder.get().setOrderStatus(OrderStatus.APPROVED);
            //save the order with the new status and return the dto
            return CustomOrderMapper.basicMapOrderToOrderDTO(
                    orderRepository.save(optionalOrder.get()));
        }
        //if order doesn't exist, throw exception
        else {
            throw new Exception("Order with ID: " + orderId + ", does not exist");
        }
    }

    @Override
    public OrderDTO managerDeclineOrder(Long orderId, String declineReason) throws Exception {
        //retrieve order
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        //check if order exists and the status is awaiting approval
        if(optionalOrder.isPresent() && (optionalOrder.get().getOrderStatus().equals(OrderStatus.AWAITING_APPROVAL))) {
            //change order status
            optionalOrder.get().setOrderStatus(OrderStatus.DECLINED);
            //set the declining reason
            optionalOrder.get().setDecliningReason(declineReason);
            //return the dto of the order with the new values
            return CustomOrderMapper.basicMapOrderToOrderDTO(
                    //save the order with the new values
                    orderRepository.save(optionalOrder.get()));
        }
        //if order doesn't exist, throw exception
        else {
            throw new Exception("Order with ID: " + orderId + ", does not exist");
        }
    }

}
