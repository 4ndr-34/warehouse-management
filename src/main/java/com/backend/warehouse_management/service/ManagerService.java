package com.backend.warehouse_management.service;

import com.backend.warehouse_management.dto.client.OrderDTO;

import java.util.List;

public interface ManagerService {

    List<OrderDTO> managerGetAllOrders();

    OrderDTO managerGetDetailedOrder(Long orderId) throws Exception;

}
