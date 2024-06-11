package com.backend.warehouse_management.service;

import com.backend.warehouse_management.dto.client.OrderDTO;

import java.util.List;

public interface ManagerService {

    List<OrderDTO> managerGetAllOrders();

    OrderDTO managerGetDetailedOrder(Long orderId) throws Exception;

    OrderDTO managerApproveOrder(Long orderId) throws Exception;

    OrderDTO managerDeclineOrder(Long orderId, String declineReason) throws Exception;

}
