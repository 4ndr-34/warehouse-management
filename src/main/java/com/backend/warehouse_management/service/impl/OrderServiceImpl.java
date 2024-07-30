package com.backend.warehouse_management.service.impl;

import com.backend.warehouse_management.dto.client.*;
import com.backend.warehouse_management.dto.manager.CreateDeliveryRequest;
import com.backend.warehouse_management.dto.manager.DeclineOrderRequest;
import com.backend.warehouse_management.dto.manager.DeliveryDTO;
import com.backend.warehouse_management.dto.manager.OrderAndDeliveryRequest;
import com.backend.warehouse_management.service.OrderService;
import com.backend.warehouse_management.utils.OrderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderUtils orderUtils;

    @Override
    public OrderDTO clientCreateOrder(Long userId) {
        return orderUtils.clientCreateOrder(userId);
    }


    @Override
    public OrderDTO clientAddItemToOrder(AddItemToOrderRequest itemRequest) {
        return orderUtils.clientAddItemToOrder(itemRequest);
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
    public OrderDTO managerGetDetailedOrder(Long orderId) {
        return orderUtils.managerGetDetailedOrder(orderId);
    }

    @Override
    public OrderDTO managerApproveOrder(Long orderId) {
        return orderUtils.managerApproveOrder(orderId);
    }

    @Override
    public OrderDTO managerDeclineOrder(DeclineOrderRequest request) {
        return orderUtils.managerDeclineOrder(request);
    }

    @Override
    public List<DeliveryDTO> managerCheckAvailableDeliveryDates() {
        return orderUtils.managerCheckAvailableDeliveryDates();
    }

    @Override
    public DeliveryDTO managerCreateDeliveryWithTruck(CreateDeliveryRequest deliveryRequest) {
        return orderUtils.managerCreateDeliveryWithTruck(deliveryRequest);
    }

    @Override
    public DeliveryDTO managerAddOrderToDelivery(OrderAndDeliveryRequest request) {
        return orderUtils.managerAddOrderToDelivery(request.getOrderId(), request.getDeliveryId());
    }

    @Override
    public DeliveryDTO completeDelivery() {
        return orderUtils.completeDelivery();
    }

    @Override
    public List<OrderDTO> getAllOrdersForClientId(Long userId) {
        return orderUtils.getAllOrdersForClientId(userId);
    }

    @Override
    public List<OrderDTO> getOrdersByStatusAndClientId(GetOrdersByStatusRequest request) {
        return orderUtils.getOrdersByStatusAndClientId(request);
    }
}
