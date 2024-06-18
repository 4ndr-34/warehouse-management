package com.backend.warehouse_management.service;

import com.backend.warehouse_management.dto.client.AddItemToOrderRequest;
import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.dto.client.RemoveOrderItemRequest;
import com.backend.warehouse_management.dto.client.UpdateOrderItemRequest;

public interface ClientService {

    OrderDTO createOrder(Long userId) throws Exception;

    OrderDTO addItemToOrder(Long userId, AddItemToOrderRequest itemRequest);

    OrderDTO updateItemQuantity(UpdateOrderItemRequest request);

    OrderDTO removeItemFromOrder(RemoveOrderItemRequest removeOrderItemRequest);

    OrderDTO submitOrder(Long orderId);

    OrderDTO cancelOrder(Long orderId);
}
