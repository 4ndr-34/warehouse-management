package com.backend.warehouse_management.service;

import com.backend.warehouse_management.dto.client.*;
import com.backend.warehouse_management.dto.manager.CreateDeliveryRequest;
import com.backend.warehouse_management.dto.manager.DeliveryDTO;
import com.backend.warehouse_management.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getAllOrdersForClientId(Long userId);

    List<OrderDTO> getOrdersByStatusAndClientId(Long userId, OrderStatus orderStatus);
    OrderDTO clientCreateOrder(Long userId) throws Exception;
    OrderDTO clientAddItemToOrder(Long userId, AddItemToOrderRequest itemRequest);
    OrderDTO clientUpdateItemQuantity(UpdateOrderItemRequest request);
    OrderDTO clientRemoveItemFromOrder(RemoveOrderItemRequest removeOrderItemRequest);
    OrderDTO clientSubmitOrder(Long orderId);
    OrderDTO clientCancelOrder(Long orderId);
    List<OrderDTO> managerGetAllOrders();
    OrderDTO managerGetDetailedOrder(Long orderId) throws Exception;
    OrderDTO managerApproveOrder(Long orderId) throws Exception;
    OrderDTO managerDeclineOrder(Long orderId, String declineReason) throws Exception;
    List<DeliveryDTO> managerCheckAvailableDeliveryDates();
    DeliveryDTO managerCreateDeliveryWithTruck(CreateDeliveryRequest deliveryRequest, Long truckId) throws Exception;
    DeliveryDTO managerAddOrderToDelivery(Long orderId, Long deliveryId) throws Exception;
    List<DeliveryDTO> managerRemoveOrderFromDelivery(Long orderId, Long deliveryId);

}
