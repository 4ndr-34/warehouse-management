package com.backend.warehouse_management.service.impl;

import com.backend.warehouse_management.dto.client.*;
import com.backend.warehouse_management.dto.manager.CreateDeliveryRequest;
import com.backend.warehouse_management.dto.manager.DeliveryDTO;
import com.backend.warehouse_management.entity.Order;
import com.backend.warehouse_management.enums.OrderStatus;
import com.backend.warehouse_management.mapper.CustomOrderMapper;
import com.backend.warehouse_management.repository.OrderRepository;
import com.backend.warehouse_management.service.OrderService;
import com.backend.warehouse_management.utils.OrderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;
    private final OrderUtils orderUtils;

    @Override
    public OrderDTO clientCreateOrder(Long userId) throws Exception {
        return orderUtils.clientCreateOrder(userId);
    }


    @Override
    public OrderDTO clientAddItemToOrder(Long userId, AddItemToOrderRequest itemRequest) {
        return orderUtils.clientAddItemToOrder(userId, itemRequest);
    }


    @Override
    public OrderDTO clientUpdateItemQuantity(UpdateOrderItemRequest request) {
        return orderUtils.clientUpdateItemQuantity(request);
    }


    @Override
    public OrderDTO clientRemoveItemFromOrder(RemoveOrderItemRequest request) {
        return orderUtils.clientRemoveItemFromOrder(request);
    }

    @Override
    public OrderDTO clientSubmitOrder(Long orderId) {
        return orderUtils.clientSubmitOrder(orderId);
    }

    @Override
    public OrderDTO clientCancelOrder(Long orderId) {
        return orderUtils.clientCancelOrder(orderId);
    }

    @Override
    public List<OrderDTO> managerGetAllOrders() {
        return orderUtils.managerGetAllOrders();
    }

    @Override
    public OrderDTO managerGetDetailedOrder(Long orderId) throws Exception {
        return orderUtils.managerGetDetailedOrder(orderId);
    }

    @Override
    public OrderDTO managerApproveOrder(Long orderId) throws Exception {
        return orderUtils.managerApproveOrder(orderId);
    }

    @Override
    public OrderDTO managerDeclineOrder(Long orderId, String declineReason) throws Exception {
        return orderUtils.managerDeclineOrder(orderId, declineReason);
    }

    @Override
    public List<DeliveryDTO> managerCheckAvailableDeliveryDates() {
        return orderUtils.managerCheckAvailableDeliveryDates();
    }

    @Override
    public DeliveryDTO managerCreateDeliveryWithTruck(CreateDeliveryRequest deliveryRequest, Long truckId) throws Exception {
        return orderUtils.managerCreateDeliveryWithTruck(deliveryRequest, truckId);
    }

    @Override
    public DeliveryDTO managerAddOrderToDelivery(Long orderId, Long deliveryId) throws Exception {
        return orderUtils.managerAddOrderToDelivery(orderId, deliveryId);
    }

    @Override
    public List<DeliveryDTO> managerRemoveOrderFromDelivery(Long orderId, Long deliveryId) {
        return orderUtils.managerRemoveOrderFromDelivery(orderId, deliveryId);
    }

    @Override
    public List<OrderDTO> getAllOrdersForClientId(Long userId) {
        List<Order> ordersForUser = orderRepository.findAllByUserId(userId);
        List<OrderDTO> orderListForReturn = new ArrayList<>();
        for(Order order : ordersForUser) {
            orderListForReturn.add(CustomOrderMapper.basicMapOrderToOrderDTO(order));
        }
        return orderListForReturn;
    }

    @Override
    public List<OrderDTO> getOrdersByStatusAndClientId(Long userId, OrderStatus orderStatus) {
        List<Order> ordersOfStatus = orderRepository.findAllByUserIdAndOrderStatus(userId, orderStatus);
        List<OrderDTO> orderListForReturn = new ArrayList<>();
        for(Order order : ordersOfStatus) {
            orderListForReturn.add(CustomOrderMapper.basicMapOrderToOrderDTO(order));
        }
        return orderListForReturn;
    }
}
