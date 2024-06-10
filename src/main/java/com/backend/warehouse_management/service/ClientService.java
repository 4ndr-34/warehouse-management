package com.backend.warehouse_management.service;

import com.backend.warehouse_management.dto.client.AddItemToOrderRequest;
import com.backend.warehouse_management.dto.client.OrderDTO;

public interface ClientService {

    OrderDTO createOrder(Long userId) throws Exception;

    OrderDTO addItemToOrder(Long userId, AddItemToOrderRequest itemRequest) throws Exception;

    OrderDTO updateItemQuantity(Long orderId, Long itemId, Integer quantity) throws Exception;

    OrderDTO removeItemFromOrder(Long itemId, Long orderId) throws Exception;

    OrderDTO submitOrder(Long userId, Long orderId) throws Exception;

    OrderDTO cancelOrder(Long userId, Long orderId) throws Exception;
}
