package com.backend.warehouse_management.service.impl;

import com.backend.warehouse_management.dto.client.AddItemToOrderRequest;
import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.dto.client.RemoveOrderItemRequest;
import com.backend.warehouse_management.dto.client.UpdateOrderItemRequest;
import com.backend.warehouse_management.service.ClientService;
import com.backend.warehouse_management.utils.OrderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final OrderUtils orderUtils;

    @Override
    public OrderDTO createOrder(Long userId) throws Exception {
        return orderUtils.createOrder(userId);
    }


    @Override
    public OrderDTO addItemToOrder(Long userId, AddItemToOrderRequest itemRequest) {
        return orderUtils.addItemToOrder(userId, itemRequest);
    }


    @Override
    public OrderDTO updateItemQuantity(UpdateOrderItemRequest request) {
        return orderUtils.updateItemQuantity(request);
    }


    @Override
    public OrderDTO removeItemFromOrder(RemoveOrderItemRequest request) {
        return orderUtils.removeItemFromOrder(request);
    }

    @Override
    public OrderDTO submitOrder(Long orderId) {
        return orderUtils.submitOrder(orderId);
    }

    @Override
    public OrderDTO cancelOrder(Long orderId) {
        return orderUtils.cancelOrder(orderId);
    }


}
