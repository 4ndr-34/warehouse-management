package com.backend.warehouse_management.service;

import com.backend.warehouse_management.dto.client.OrderDTO;
import com.backend.warehouse_management.dto.manager.CreateDeliveryRequest;
import com.backend.warehouse_management.dto.manager.DeliveryDTO;

import java.util.List;

public interface ManagerService {

    List<OrderDTO> managerGetAllOrders();

    OrderDTO managerGetDetailedOrder(Long orderId) throws Exception;

    OrderDTO managerApproveOrder(Long orderId) throws Exception;

    OrderDTO managerDeclineOrder(Long orderId, String declineReason) throws Exception;

    //check deliveries available for the upcoming dates
    List<DeliveryDTO> managerCheckAvailableDeliveryDates();

    DeliveryDTO managerCreateDeliveryWithTruck(CreateDeliveryRequest deliveryRequest, Long truckId) throws Exception;
    DeliveryDTO managerAddOrderToDelivery(Long orderId, Long deliveryId) throws Exception;

    List<DeliveryDTO> managerRemoveOrderFromDelivery(Long orderId, Long deliveryId);
}
