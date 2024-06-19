package com.backend.warehouse_management.service;

import com.backend.warehouse_management.dto.client.*;
import com.backend.warehouse_management.dto.manager.AddOrderToDeliveryRequest;
import com.backend.warehouse_management.dto.manager.CreateDeliveryRequest;
import com.backend.warehouse_management.dto.manager.DeclineOrderRequest;
import com.backend.warehouse_management.dto.manager.DeliveryDTO;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getAllOrdersForClientId(Long userId);
    List<OrderDTO> getOrdersByStatusAndClientId(GetOrdersByStatusRequest request);
    OrderDTO clientCreateOrder(Long userId) throws Exception;
    OrderDTO clientAddItemToOrder(AddItemToOrderRequest itemRequest);
    OrderDTO clientUpdateItemQuantity(UpdateOrderItemRequest request);
    OrderDTO clientRemoveItemFromOrder(RemoveOrderItemRequest removeOrderItemRequest);
    OrderDTO clientSubmitOrder(Long orderId);
    OrderDTO clientCancelOrder(Long orderId);
    List<OrderDTO> managerGetAllOrders();
    OrderDTO managerGetDetailedOrder(Long orderId);
    OrderDTO managerApproveOrder(Long orderId);
    OrderDTO managerDeclineOrder(DeclineOrderRequest request);
    List<DeliveryDTO> managerCheckAvailableDeliveryDates();
    DeliveryDTO managerCreateDeliveryWithTruck(CreateDeliveryRequest deliveryRequest);
    DeliveryDTO managerAddOrderToDelivery(AddOrderToDeliveryRequest request);
    List<DeliveryDTO> managerRemoveOrderFromDelivery(Long orderId, Long deliveryId);

}
